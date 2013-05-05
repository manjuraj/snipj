package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {

    public static void main(String[] args) throws Exception {
        String infile = args[0];
        String outfile = args[1];

        // file -> stream
        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        // stream -> channel
        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        // buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // channel -> buffer
        while (true) {
            // reset the buffer and make it ready to have data read into it
            buffer.clear();

            // input channel -> buffer
            int r = fcin.read(buffer);
            if (r < 0) {
                break;
            }

            buffer.flip();

            // buffer -> output channel
            fcout.write(buffer);
        }
    }
}
