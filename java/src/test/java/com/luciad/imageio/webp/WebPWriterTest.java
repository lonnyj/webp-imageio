package com.luciad.imageio.webp;

import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <code>WebPWriterTest</code> unit tests {@link WebPWriter}.
 */
public class WebPWriterTest {
   /** The "lossy" compression type. */
   private static final String LOSSY_COMPRESSION_TYPE = "Lossy";

   /** The "lossless" compression type. */
   private static final String LOSSLESS_COMPRESSION_TYPE = "Lossless";

   /**
    * Tests writing WebP images using
    * {@link ImageIO#write(RenderedImage, String, java.io.OutputStream)}.
    * 
    * @param im
    *           the image to write.
    * @param outputName
    *           the name of the file to write.
    * @throws IOException
    *            if unable to write the image.
    */
   @Test(dataProvider = "testImages", enabled = true)
   public void testImageIoWrite(final RenderedImage im, final String outputName) throws IOException {
      final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
      ImageIO.write(im, "webp", baoStream);
      assertTrue(baoStream.size() > 0);
      final String testName = "ImageIoWrite";
      File file = createOutputFile(testName, outputName);
      final FileOutputStream foStream = new FileOutputStream(file);
      baoStream.writeTo(foStream);
      foStream.close();
   }

   /**
    * The data provider for {@link #testImageIoWrite}.
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

   /**
    * Test method for
    * {@link com.luciad.imageio.webp.WebPWriter#write(javax.imageio.metadata.IIOMetadata, javax.imageio.IIOImage, javax.imageio.ImageWriteParam)}
    * .
    *
    * @throws IOException
    */
   @Test(dataProvider = "testImageOptions", enabled = true)
   public void testWriteIIOMetadataIIOImageImageWriteParam(final RenderedImage im,
         final String compressionType, final float compressionQuality, final String outputName)
         throws IOException {
      final String extension = outputName.substring(outputName.lastIndexOf(".") + 1);

      // get writer
      final ImageWriter imgWriter = ImageIO.getImageWritersByFormatName(extension).next();
      final ImageWriteParam imgWriteParams = new WebPWriteParam(null);
      imgWriteParams.setCompressionType(compressionType);
      // 0~1
      imgWriteParams.setCompressionQuality(compressionQuality);
      final String testName = "ImageWriter";
      File file = createOutputFile(testName, outputName);
      final ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(file);
      try {
         imgWriter.setOutput(imageOutputStream);
         imgWriter.write(null, new IIOImage(im, null, null), imgWriteParams);
         final int length = (int) imageOutputStream.length();
         assertTrue(length > 0);
      } finally {
         try {
            imageOutputStream.close();
         } catch (final IOException e) {
         }
      }
   }

   /**
    * Creates the output file in the specified test directory.
    * 
    * @param testName
    *           the name of the test to use as the output directory name.
    * @param outputName
    *           the name of the file to be created.
    * @return the file destination.
    */
   private File createOutputFile(final String testName, final String outputName) {
      final File parentDirectory = new File("target" + File.separator + testName);
      parentDirectory.mkdirs();
      File file = new File(parentDirectory, outputName);
      return file;
   }

   /**
    * The data provider for all test images.
    *
    * @return the test {@link BufferedImage}s.
    * @throws IOException
    *            if unable to read one of the test images.
    */
   @DataProvider
   public Object[][] testImageOptions() throws IOException {
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      return new Object[][] {
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSY_COMPRESSION_TYPE, 0f, "1_lossy_0.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSY_COMPRESSION_TYPE, 0.5f, "1_lossy_50.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSY_COMPRESSION_TYPE, 0.9f, "1_lossy_90.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSY_COMPRESSION_TYPE, 1f, "1_lossy_100.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSLESS_COMPRESSION_TYPE, 0f, "1_lossless_0.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSLESS_COMPRESSION_TYPE, 0.5f, "1_lossless_50.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSLESS_COMPRESSION_TYPE, 0.9f, "1_lossless_90.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("1.png")), LOSSLESS_COMPRESSION_TYPE, 1f, "1_lossless_100.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("2.png")), LOSSY_COMPRESSION_TYPE, 0.9f, "2_lossy_90.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("3.png")), LOSSY_COMPRESSION_TYPE, 0.9f, "3_lossy_90.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("4.png")), LOSSY_COMPRESSION_TYPE, 0.9f, "4_lossy_90.webp" },
            new Object[] { ImageIO.read(classLoader.getResource("5.png")), LOSSY_COMPRESSION_TYPE, 0.9f, "5_lossy_90.webp" } };
   }
}
