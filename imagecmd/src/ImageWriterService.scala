import java.awt.image.BufferedImage
import zio.{Has, Task, ZLayer, ZIO}

/**
  * See https://zio.dev/docs/howto/howto_use_layers
  */
object ImageWriter {

  type ImageWriter = Has[ImageWriter.Service]

  trait Service {
    def writeImage(img: BufferedImage, fileName: String): Task[Unit]
  }

  def writeImage(
      img: BufferedImage,
      fileName: String
  ): ZIO[ImageWriter, Throwable, Unit] =
    ZIO.accessM(_.get.writeImage(img, fileName))
}
