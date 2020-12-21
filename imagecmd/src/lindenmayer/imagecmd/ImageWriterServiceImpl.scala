package lindenmayer.imagecmd

import java.io.File
import cats.effect.Sync
import cats.Monad
import scala.util.Try
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import zio.interop.catz.taskEffectInstance
import zio._

class ImageWriterServiceImpl[F[_]: Sync] extends ImageWriter[F] {

  override def writeImage(
      img: BufferedImage,
      fileName: String
  ): F[Unit] =
    implicitly[Sync[F]].delay(ImageIO.write(img, "jpg", new File(fileName)))
}

object ImageWriterZIO {

  type ImageWriterZIO = Has[ImageWriter[Task]]

  implicit val runtime: Runtime[ZEnv] = Runtime.default

  val imageWriterZIOLayer: ULayer[ImageWriterZIO] =
    ZLayer.succeed(new ImageWriterServiceImpl[Task])

  def writeImage(
      img: BufferedImage,
      fileName: String
  ): RIO[ImageWriterZIO, Unit] =
    ImageWriterZIO.writeImage(img, fileName)
}
