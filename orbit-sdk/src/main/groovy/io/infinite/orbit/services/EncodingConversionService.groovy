package io.infinite.orbit.services


import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import org.springframework.stereotype.Controller

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class EncodingConversionService {

    void convertEncoding(InputStream inputStream
                         , OutputStream outputStream
                         , String fromCharsetName
                         , String toCharsetName
                         , Integer width
    ) {
        if (width == null || width < 1) {
            outputStream << new InputStreamReader(inputStream, Charset.forName(fromCharsetName.trim()))
        } else {
            byte[] bytes = new byte[width]
            Charset toCharset = Charset.forName(toCharsetName.trim())
            Charset fromCharset = Charset.forName(fromCharsetName.trim())
            ByteBuffer fromByteBuffer
            CharBuffer fromCharBuffer
            while (inputStream.read(bytes) > 0) {
                fromByteBuffer = ByteBuffer.wrap(bytes)
                fromCharBuffer = fromCharset.decode(fromByteBuffer)
                outputStream << toCharset.encode(fromCharBuffer).array()
                outputStream << "\n"
            }
        }
    }

}
