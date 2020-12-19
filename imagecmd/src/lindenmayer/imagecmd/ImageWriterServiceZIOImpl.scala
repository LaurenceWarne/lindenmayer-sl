package lindenmayer.imagecmd

import java.io.File
import cats.MonadError
import scala.util.Try
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import zio.{Has, RIO, Task, ULayer, ZLayer}

object ImageWriterServiceZIOImpl extends ImageWriter[Task] {

  override def writeImage(
      img: BufferedImage,
      fileName: String
  )(implicit
      E: MonadError[Task, Throwable]
  ): Task[Unit] =
    Task.effect(ImageIO.write(img, "jpg", new File(fileName)))
}

object ImageWriterZIO {

  type ImageWriterZIO = Has[ImageWriter[Task]]

  val imageWriterZIOLayer: ULayer[ImageWriterZIO] =
    ZLayer.succeed(ImageWriterServiceZIOImpl)

  def writeImage(
      img: BufferedImage,
      fileName: String
  )(implicit
      E: MonadError[Task, Throwable]
  ): RIO[ImageWriterZIO, Unit] =
    ImageWriterZIO.writeImage(img, fileName)
}
