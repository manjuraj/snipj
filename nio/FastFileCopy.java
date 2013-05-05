package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FastFileCopy {

    public static void main(String[] args) throws Exception {
        String infile = args[0];
        String outfile = args[1];

        // file -> stream
        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        // stream -> channel
        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        // direct buffer
        // from spec: given a direct byte buffer, the Java virtual machine
        // will make a best effort to perform native I/O operations directly
        // upon it. That is, it will attempt to avoid copying the buffer's
        // content to (or from) an intermediate buffer before (or after)
        // each invocation of one of the underlying operating system's
        // native I/O operation
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

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
