package com.luciad.imageio.webp;

import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.Interpolation;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ScaleDescriptor;

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
   @Test(dataProvider = "createBasicImages", enabled = true)
   public void testImageIoWrite(final RenderedImage im, final String outputName) throws IOException {
      final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
      ImageIO.write(im, "webp", baoStream);
      assertTrue(baoStream.size() > 0);
      final String testName = "ImageIo";
      final File file = createOutputFile(testName, outputName);
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
   public Object[][] createBasicImages() throws IOException {
      return new Object[][] { new Object[] { loadImage("1.png"), "1.webp" },
            new Object[] { loadImage("2.png"), "2.webp" },
            new Object[] { loadImage("3.png"), "3.webp" },
            new Object[] { loadImage("4.png"), "4.webp" },
            new Object[] { loadImage("5.png"), "5.webp" } };
   }

   /**
    * Tests reading WebP images using {@link ImageIO#read(File)}. The image is
    * then written as a PNG in the original directory.
    * 
    * @param testName
    *           the name of the directory containing the test image.
    * @param fileName
    *           the name of the test image.
    * @throws IOException
    *            if unable to read or write images.
    */
   @Test(dataProvider = "getWebPImages", alwaysRun = true, dependsOnMethods = { "testImageIoWrite",
         "testImageWriterCompression", "testImageWriterScale" })
   public void testImageIoRead(final String testName, final String fileName) throws IOException {
      final String inputName = "target" + File.separator + testName + File.separator + fileName;
      final BufferedImage image = ImageIO.read(new File(inputName));
      final String outputName = fileName.substring(0, fileName.lastIndexOf('.') + 1) + "png";
      final File file = createOutputFile(testName, outputName);
      ImageIO.write(image, "png", file);
   }

   /**
    * The data provider for {@link #testImageIoRead}. Uses all of the images
    * written from the other tests as test images for reading.
    *
    * @return the test data.
    */
   @DataProvider
   public Object[][] getWebPImages() {
      final List<Object[]> data = new ArrayList<Object[]>();
      for (final String testName : new String[] { "ImageIo", "CompressionOptions", "ScaleOptions" }) {
         final File parentDirectory = new File("target" + File.separator + testName);
         final String[] images = parentDirectory.list(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
               return name.endsWith(".webp");
            }
         });
         for (final String name : images) {
            data.add(new Object[] { testName, name });
         }
      }
      return data.toArray(new Object[data.size()][]);
   }

   /**
    * Test method for
    * {@link com.luciad.imageio.webp.WebPWriter#write(javax.imageio.metadata.IIOMetadata, javax.imageio.IIOImage, javax.imageio.ImageWriteParam)}
    * .
    *
    * @throws IOException
    */
   @Test(dataProvider = "createImagesWithCompressionOptions", enabled = true)
   public void testImageWriterCompression(final RenderedImage im, final String compressionType,
         final float compressionQuality, final String outputName) throws IOException {
      final String extension = outputName.substring(outputName.lastIndexOf(".") + 1);

      // get writer
      final ImageWriter imgWriter = ImageIO.getImageWritersByFormatName(extension).next();
      final ImageWriteParam imgWriteParams = new WebPWriteParam(null);
      imgWriteParams.setCompressionType(compressionType);
      // 0~1
      imgWriteParams.setCompressionQuality(compressionQuality);
      final String testName = "CompressionOptions";
      final File file = createOutputFile(testName, outputName);
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
    * The data provider for
    * {@link #testImageWriter(RenderedImage, String, float, String)}.
    *
    * @return the test images and options.
    * @throws IOException
    *            if unable to read one of the test images.
    */
   @DataProvider
   public Object[][] createImagesWithCompressionOptions() throws IOException {
      return new Object[][] {
            new Object[] { loadImage("1.png"), LOSSY_COMPRESSION_TYPE, 0f, "1_lossy_0.webp" },
            new Object[] { loadImage("1.png"), LOSSY_COMPRESSION_TYPE, 0.5f, "1_lossy_50.webp" },
            new Object[] { loadImage("1.png"), LOSSY_COMPRESSION_TYPE, 0.9f, "1_lossy_90.webp" },
            new Object[] { loadImage("1.png"), LOSSY_COMPRESSION_TYPE, 1f, "1_lossy_100.webp" },
            new Object[] { loadImage("1.png"), LOSSLESS_COMPRESSION_TYPE, 0f, "1_lossless_0.webp" },
            new Object[] { loadImage("1.png"), LOSSLESS_COMPRESSION_TYPE, 0.5f,
                  "1_lossless_50.webp" },
            new Object[] { loadImage("1.png"), LOSSLESS_COMPRESSION_TYPE, 1f, "1_lossless_100.webp" },
            new Object[] { loadImage("2.png"), LOSSY_COMPRESSION_TYPE, 0.9f, "2_lossy_90.webp" },
            new Object[] { loadImage("3.png"), LOSSY_COMPRESSION_TYPE, 0.9f, "3_lossy_90.webp" },
            new Object[] { loadImage("4.png"), LOSSY_COMPRESSION_TYPE, 0.9f, "4_lossy_90.webp" },
            new Object[] { loadImage("5.png"), LOSSY_COMPRESSION_TYPE, 0.9f, "5_lossy_90.webp" } };
   }

   /**
    * Test method tests {@link WebPWriter} with image resize options.
    *
    * @throws IOException
    *            the test fails.
    */
   @Test(dataProvider = "createImagesWithScaleOptions", enabled = true)
   public void testImageWriterScale(final RenderedImage image, final float xScale,
         final float yScale, final String outputName) throws IOException {
      final String extension = outputName.substring(outputName.lastIndexOf(".") + 1);

      // Scale the image.
      final RenderedOp scaledImage = ScaleDescriptor.create(image, xScale, yScale, 0f, 0f,
            Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2), null);

      // get writer
      final ImageWriter imgWriter = ImageIO.getImageWritersByFormatName(extension).next();
      final ImageWriteParam imgWriteParams = new WebPWriteParam(null);
      final String testName = "ScaleOptions";
      final File file = createOutputFile(testName, outputName);
      final ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(file);
      try {
         imgWriter.setOutput(imageOutputStream);
         imgWriter.write(null, new IIOImage(scaledImage, null, null), imgWriteParams);
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
    * The data provider for
    * {@link #testImageWriterScale(RenderedImage, float, float, String)}.
    *
    * @return the test images and options.
    * @throws IOException
    *            if unable to read one of the test images.
    */
   @DataProvider
   public Object[][] createImagesWithScaleOptions() throws IOException {
      return new Object[][] { new Object[] { loadImage("1.png"), 1, 2, "1_1x2.webp" },
            new Object[] { loadImage("2.png"), 2, 1, "2_2x1.webp" },
            new Object[] { loadImage("3.png"), 1, 5, "3_1x5.webp" },
            new Object[] { loadImage("4.png"), 5, 1, "4_5x1.webp" },
            new Object[] { loadImage("5.png"), 5, 5, "5_5x5.webp" },
            new Object[] { loadImage("1.png"), 1, .5f, "1_1x.5.webp" },
            new Object[] { loadImage("2.png"), .5f, 1, "2_.5x1.webp" },
            new Object[] { loadImage("3.png"), 1, .2f, "3_1x.2.webp" },
            new Object[] { loadImage("4.png"), .2f, 1, "4_.2x1.webp" },
            new Object[] { loadImage("5.png"), .2f, .2f, "5_.2x.2.webp" } };
   }

   /**
    * Loads the image with the given name.
    *
    * @param name
    *           the name of the image to load.
    * @return the requested image.
    * @throws IOException
    *            if unable to read the image.
    */
   private RenderedImage loadImage(final String name) throws IOException {
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      return ImageIO.read(classLoader.getResource(name));
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
      final File file = new File(parentDirectory, outputName);
      return file;
   }
}
