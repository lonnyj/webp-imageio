package com.luciad.imageio.webp;

import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <code>WebPWriterTest</code> ...
 */
public class WebPWriterTest {

   /**
    * Test method for
    * {@link com.luciad.imageio.webp.WebPWriter#write(javax.imageio.metadata.IIOMetadata, javax.imageio.IIOImage, javax.imageio.ImageWriteParam)}
    * .
    *
    * @throws IOException
    */
   @Test(dataProvider = "testImages", enabled = true)
   public void testWriteIIOMetadataIIOImageImageWriteParam(final RenderedImage im,
         final String outputName) throws IOException {
      final String extension = outputName.substring(outputName.lastIndexOf(".") + 1);

      // get writer
      final ImageWriter imgWriter = ImageIO.getImageWritersByFormatName(extension).next();
      final ImageWriteParam imgWriteParams = new WebPWriteParam(null);
      final String compressionType = imgWriteParams.getCompressionTypes()[0];
      imgWriteParams.setCompressionType(compressionType);
      final float compressionQuality = 0.9f;
      // 0~1
      imgWriteParams.setCompressionQuality(compressionQuality);
      final OutputStream byteArrayOutputStream = new FileOutputStream(outputName);
      try {
         final ImageOutputStream imageOutputStream = ImageIO
               .createImageOutputStream(byteArrayOutputStream);
         imgWriter.setOutput(imageOutputStream);
         imgWriter.write(null, new IIOImage(im, null, null), imgWriteParams);
         final int length = (int) imageOutputStream.length();
         assertTrue(length > 0);
         // byte[] resultData = new byte[length];
         // imageOutputStream.read(resultData, 0, length);
         // return resultData;
      } finally {
         try {
            byteArrayOutputStream.close();
         } catch (final IOException e) {
         }
      }
   }

   @Test(dataProvider = "testImages", enabled = true)
   public void testImageIoWrite(final RenderedImage im, final String outputName) throws IOException {
      final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
      ImageIO.write(im, "webp", baoStream);
      assertTrue(baoStream.size() > 0);
      final FileOutputStream foStream = new FileOutputStream(outputName);
      baoStream.writeTo(foStream);
      foStream.close();
   }

   /**
    * The data provider for all test images.
    *
    * @return the test {@link BufferedImage}s.
    * @throws IOException
    *            if unable to read one of the test images.
    */
   @DataProvider
   public Object[][] testImages() throws IOException {
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      return new Object[][] {
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), "1.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("2.png")), "2.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("3.png")), "3.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("4.png")), "4.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("5.png")), "5.webp" } };
   }
}
