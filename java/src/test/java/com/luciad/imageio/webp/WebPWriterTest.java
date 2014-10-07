package com.luciad.imageio.webp;

import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <code>WebPWriterTest</code> ...
 */
public class WebPWriterTest {

   @BeforeSuite
   public void beforeSuite() {
      String libraryPath = System.getProperty("java.library.path");
      if (libraryPath == null || "".equals(libraryPath)) {
         libraryPath = ".." + File.separator + "c";
      } else {
         libraryPath = libraryPath + File.pathSeparator + ".." + File.separator + "c";
      }
   }

   /**
    * Test method for
    * {@link com.luciad.imageio.webp.WebPWriter#write(javax.imageio.metadata.IIOMetadata, javax.imageio.IIOImage, javax.imageio.ImageWriteParam)}
    * .
    */
   @Test(enabled = false)
   public void testWriteIIOMetadataIIOImageImageWriteParam() {
      fail("Not yet implemented");
   }

   @Test(dataProvider = "testImages")
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
