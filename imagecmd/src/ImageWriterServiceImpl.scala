package imagecmd

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import zio.Task

class ImageWriterServiceImpl extends ImageWriter.Service {

  override def writeImage(
      img: BufferedImage,
      fileName: String
  ): zio.Task[Unit] =
    Task.effect(ImageIO.write(img, "jpg", new File(fileName)))
}
