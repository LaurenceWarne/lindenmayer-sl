package lindenmayer.imagecmd

import java.awt.image.BufferedImage

trait ImageWriter[F[_]] {
  def writeImage(
      img: BufferedImage,
      fileName: String
  ): F[Unit]
}
