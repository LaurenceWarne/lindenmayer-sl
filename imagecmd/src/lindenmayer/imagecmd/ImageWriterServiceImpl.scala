package lindenmayer.imagecmd

import java.io.File
import scala.util.Try
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import zio.{Task, ZIO}

class ImageWriterServiceImpl extends ImageWriter.Service {

  override def writeImage(
      img: BufferedImage,
      fileName: String
  ): zio.Task[Unit] =
    ZIO.fromTry(Try(ImageIO.write(img, "jpg", new File(fileName))))
}
