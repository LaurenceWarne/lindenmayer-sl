package lindenmayer.imagecmd

import java.awt.image.BufferedImage
import cats.MonadError
import zio.{Has, Task, ZIO}

trait ImageWriter[F[_]] {
  def writeImage(img: BufferedImage, fileName: String)(implicit
      E: MonadError[F, Throwable]
  ): F[Unit]
}
