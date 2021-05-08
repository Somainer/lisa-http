import java.io.{InputStream, ByteArrayInputStream}
import java.nio.charset.StandardCharsets
import moe.roselia.lisa.LispExp._
import moe.roselia.lisa.Annotation.RawLisa

class RangeInputStream(stream: InputStream, from: Long, to: Long) extends InputStream {
  stream.skip(from)
  private var cursor = from
  override def available(): Int = availableBytes.toInt
  def availableBytes = to - from
  
  override def read(): Int = {
    val result = {
      if (cursor < to) stream.read() else -1
    }
    cursor += 1
    result
  }

  override def read(b: Array[Byte], off: Int, len: Int): Int = {
    val available = availableBytes
    if (len > available) {
      cursor = to
      stream.read(b, off, available.toInt)
    } else {
      cursor += len
      stream.read(b, off, len)
    }
  }
  
  override def close(): Unit = {
    stream.close()
  }
}

PrimitiveFunction {
  case WrappedScalaObject(stream: InputStream) :: SInteger(from) :: SInteger(to) :: Nil =>
    WrappedScalaObject(new RangeInputStream(stream, from.toLong, to.toLong))
  case SString(str) :: Nil =>
    val buf = StandardCharsets.UTF_8.encode(str)
    val inputStream = new ByteArrayInputStream(buf.array())
    WrappedScalaObject(new RangeInputStream(inputStream, buf.position().toLong, buf.limit().toLong))
}