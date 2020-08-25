package com.example.Calls.BackEnd.CheapSound;

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.SharedVariables;

/**
 * CheapSoundFile is the parent class of several subclasses that each do a
 * "cheap" scan of various sound file formats, parsing as little as possible in
 * order to understand the high-level frame structure and get a rough estimate
 * of the volume level of each frame. Each subclass is able to: - open a sound
 * file - return the sample rate and number of frames - return an approximation
 * of the volume level of each frame - write a new sound file with a subset of
 * the frames
 *
 * A frame should represent no less than 1 ms and no more than 100 ms of audio.
 * This is compatible with the native frame sizes of most audio file formats
 * already, but if not, this class should expose virtual frames in that size
 * range.
 */
public class CheapSoundFile {


    // Member variables representing frame data
    private String mFileType;
    private int mFileSize;
    private int mAvgBitRate;  // Average bit rate in kbps.
    private int mSampleRate;
    private int mChannels;
    private int mNumSamples;  // total number of samples per channel in audio file
    private ByteBuffer mDecodedBytes;  // Raw audio data
    private ShortBuffer mDecodedSamples;  // shared buffer with mDecodedBytes.





    public interface ProgressListener {
        /**
         * Will be called by the CheapSoundFile subclass periodically with
         * values between 0.0 and 1.0. Return true to continue loading the file,
         * and false to cancel.
         */
        boolean reportProgress(double fractionComplete);
    }

    public interface Factory {
        CheapSoundFile create();

        String[] getSupportedExtensions();
    }

    static Factory[] sSubclassFactories = new Factory[] {
            CheapAAC.getFactory(), CheapAMR.getFactory(),
            CheapMP3.getFactory(), CheapWAV.getFactory(), };

    static ArrayList<String> sSupportedExtensions = new ArrayList<String>();
    static HashMap<String, Factory> sExtensionMap = new HashMap<String, Factory>();

    static {
        for (Factory f : sSubclassFactories) {
            for (String extension : f.getSupportedExtensions()) {
                sSupportedExtensions.add(extension);
                sExtensionMap.put(extension, f);
            }
        }
    }

    /**
     * Static method to create the appropriate CheapSoundFile subclass given a
     * filename.
     *
     * TODO: make this more modular rather than hardcoding the logic
     */
    public static CheapSoundFile create(String fileName
    )
            throws java.io.IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            throw new java.io.FileNotFoundException(fileName);
        }
        String name = f.getName().toLowerCase();
        String[] components = name.split("\\.");
        if (components.length < 2) {
            return null;
        }
        Factory factory = sExtensionMap.get(components[components.length - 1]);
        if (factory == null) {
            return null;
        }
        CheapSoundFile soundFile = factory.create();
        // soundFile.setProgressListener(progressListener);
        soundFile.ReadFile(f);
        return soundFile;
    }

    public static boolean isFilenameSupported(String filename) {
        String[] components = filename.toLowerCase().split("\\.");
        if (components.length < 2) {
            return false;
        }
        return sExtensionMap.containsKey(components[components.length - 1]);
    }

    /**
     * Return the filename extensions that are recognized by one of our
     * subclasses.
     */
    public static String[] getSupportedExtensions() {
        return sSupportedExtensions.toArray(new String[sSupportedExtensions
                .size()]);
    }

    protected ProgressListener mProgressListener = null;
    protected File mInputFile = null;

    protected CheapSoundFile() {

    }

    public void ReadFile(File inputFile) throws
            java.io.IOException {
        mInputFile = inputFile;
    }

    public void setProgressListener(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    public int getNumFrames() {
        return 0;
    }

    public int getSamplesPerFrame() {
        return 0;
    }

    public int[] getFrameOffsets() {
        return null;
    }

    public int[] getFrameLens() {
        return null;
    }

    public int[] getFrameGains() {
        return null;
    }

    public int getFileSizeBytes() {
        return 0;
    }

    public int getAvgBitrateKbps() {
        return 0;
    }

    public int getSampleRate() {
        return 0;
    }

    public int getChannels() {
        return 0;
    }

    public String getFiletype() {
        return "Unknown";
    }

    /**
     * If and only if this particular file format supports seeking directly into
     * the middle of the file without reading the rest of the header, this
     * returns the byte offset of the given frame, otherwise returns -1.
     */
    public int getSeekableFrameOffset(int frame) {
        return -1;
    }

    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String bytesToHex(byte[] hash) {
        char[] buf = new char[hash.length * 2];
        for (int i = 0, x = 0; i < hash.length; i++) {
            buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
            buf[x++] = HEX_CHARS[hash[i] & 0xf];
        }
        return new String(buf);
    }

    public String computeMd5OfFirst10Frames()
            throws java.io.IOException,
            java.security.NoSuchAlgorithmException {
        int[] frameOffsets = getFrameOffsets();
        int[] frameLens = getFrameLens();
        int numFrames = frameLens.length;
        if (numFrames > 10) {
            numFrames = 10;
        }

        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        FileInputStream in = new FileInputStream(mInputFile);
        int pos = 0;
        for (int i = 0; i < numFrames; i++) {
            int skip = frameOffsets[i] - pos;
            int len = frameLens[i];
            if (skip > 0) {
                in.skip(skip);
                pos += skip;
            }
            byte[] buffer = new byte[len];
            in.read(buffer, 0, len);
            digest.update(buffer);
            pos += len;
        }
        in.close();
        byte[] hash = digest.digest();
        return bytesToHex(hash);
    }

    public void WriteFile(File outputFile, int startFrame, int numFrames, String test)
            throws java.io.IOException {
        float startTime = (float)startFrame * getSamplesPerFrame() / mSampleRate;
        float endTime = (float)(startFrame + numFrames) * getSamplesPerFrame() / mSampleRate;
        WriteFile(outputFile, startTime, endTime);
    }

    public void WriteFile(File outputFile, float startTime, float endTime)
            throws java.io.IOException {
        int startOffset = (int)(startTime * mSampleRate) * 2 * mChannels;
        int numSamples = (int)((endTime - startTime) * mSampleRate);
        // Some devices have problems reading mono AAC files (e.g. Samsung S3). Making it stereo.
        int numChannels = (mChannels == 1) ? 2 : mChannels;

        String mimeType = "audio/mp4a-latm";
        int bitrate = 16000 * numChannels;  // rule of thumb for a good quality: 64kbps per channel.
        Log.d("bitrate", String.valueOf(bitrate));
        MediaCodec codec = MediaCodec.createEncoderByType(mimeType);
        MediaFormat format = MediaFormat.createAudioFormat(mimeType, mSampleRate, numChannels);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        codec.start();

        // Get an estimation of the encoded data based on the bitrate. Add 10% to it.
        int estimatedEncodedSize = (int)((endTime - startTime) * (bitrate / 8) * 1.1);
        ByteBuffer encodedBytes = ByteBuffer.allocate(estimatedEncodedSize);
        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean done_reading = false;
        long presentation_time = 0;

        int frame_size = 1024;  // number of samples per frame per channel for an mp4 (AAC) stream.
        byte buffer[] = new byte[frame_size * numChannels * 2];  // a sample is coded with a short.
        mDecodedBytes.position(startOffset);
        numSamples += (2 * frame_size);  // Adding 2 frames, Cf. priming frames for AAC.
        int tot_num_frames = 1 + (numSamples / frame_size);  // first AAC frame = 2 bytes
        if (numSamples % frame_size != 0) {
            tot_num_frames++;
        }
        int[] frame_sizes = new int[tot_num_frames];
        int num_out_frames = 0;
        int num_frames=0;
        int num_samples_left = numSamples;
        int encodedSamplesSize = 0;  // size of the output buffer containing the encoded samples.
        byte[] encodedSamples = null;
        while (true) {
            // Feed the samples to the encoder.
            int inputBufferIndex = codec.dequeueInputBuffer(100);
            if (!done_reading && inputBufferIndex >= 0) {
                if (num_samples_left <= 0) {
                    // All samples have been read.
                    codec.queueInputBuffer(
                            inputBufferIndex, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    done_reading = true;
                } else {
                    inputBuffers[inputBufferIndex].clear();
                    if (buffer.length > inputBuffers[inputBufferIndex].remaining()) {
                        // Input buffer is smaller than one frame. This should never happen.
                        continue;
                    }
                    // bufferSize is a hack to create a stereo file from a mono stream.
                    int bufferSize = (mChannels == 1) ? (buffer.length / 2) : buffer.length;
                    if (mDecodedBytes.remaining() < bufferSize) {
                        for (int i=mDecodedBytes.remaining(); i < bufferSize; i++) {
                            buffer[i] = 0;  // pad with extra 0s to make a full frame.
                        }
                        mDecodedBytes.get(buffer, 0, mDecodedBytes.remaining());
                    } else {
                        mDecodedBytes.get(buffer, 0, bufferSize);
                    }
                    if (mChannels == 1) {
                        for (int i=bufferSize - 1; i >= 1; i -= 2) {
                            buffer[2*i + 1] = buffer[i];
                            buffer[2*i] = buffer[i-1];
                            buffer[2*i - 1] = buffer[2*i + 1];
                            buffer[2*i - 2] = buffer[2*i];
                        }
                    }
                    num_samples_left -= frame_size;
                    inputBuffers[inputBufferIndex].put(buffer);
                    presentation_time = (long) (((num_frames++) * frame_size * 1e6) / mSampleRate);
                    codec.queueInputBuffer(
                            inputBufferIndex, 0, buffer.length, presentation_time, 0);
                }
            }

            // Get the encoded samples from the encoder.
            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100);
            if (outputBufferIndex >= 0 && info.size > 0 && info.presentationTimeUs >=0) {
                if (num_out_frames < frame_sizes.length) {
                    frame_sizes[num_out_frames++] = info.size;
                }
                if (encodedSamplesSize < info.size) {
                    encodedSamplesSize = info.size;
                    encodedSamples = new byte[encodedSamplesSize];
                }
                outputBuffers[outputBufferIndex].get(encodedSamples, 0, info.size);
                outputBuffers[outputBufferIndex].clear();
                codec.releaseOutputBuffer(outputBufferIndex, false);
                if (encodedBytes.remaining() < info.size) {  // Hopefully this should not happen.
                    estimatedEncodedSize = (int)(estimatedEncodedSize * 1.2);  // Add 20%.
                    ByteBuffer newEncodedBytes = ByteBuffer.allocate(estimatedEncodedSize);
                    int position = encodedBytes.position();
                    encodedBytes.rewind();
                    newEncodedBytes.put(encodedBytes);
                    encodedBytes = newEncodedBytes;
                    encodedBytes.position(position);
                }
                encodedBytes.put(encodedSamples, 0, info.size);
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = codec.getOutputBuffers();
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // Subsequent data will conform to new format.
                // We could check that codec.getOutputFormat(), which is the new output format,
                // is what we expect.
            }
            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                // We got all the encoded data from the encoder.
                break;
            }
        }
        int encoded_size = encodedBytes.position();
        encodedBytes.rewind();
        codec.stop();
        codec.release();
        codec = null;

        // Write the encoded stream to the file, 4kB at a time.
        buffer = new byte[4096];
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(
                    MP4Header.getMP4Header(mSampleRate, numChannels, frame_sizes, bitrate));
            while (encoded_size - encodedBytes.position() > buffer.length) {
                encodedBytes.get(buffer);
                outputStream.write(buffer);
            }
            int remaining = encoded_size - encodedBytes.position();
            if (remaining > 0) {
                encodedBytes.get(buffer, 0, remaining);
                outputStream.write(buffer, 0, remaining);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Ringdroid", "Failed to create the .m4a file.");
        }
    }


}