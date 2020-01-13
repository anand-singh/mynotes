package core

sealed trait NoteError
case object NotFound extends NoteError
case object AlreadyExists extends NoteError
case object InvalidNoteTitle extends NoteError

trait Notes[F[_], Title, Tag, Content] {
  def listAllNotes:F[Either[NoteError,List[Note]]]
  def create:Title => Content => F[Either[NoteError,Note]]
  def findByTitle:Title => F[Either[NoteError,Note]]
  def findByTag:Tag => F[Either[NoteError,List[Note]]]
  def modify:Title => F[Either[NoteError,Note]]
  def delete:Title => F[Either[NoteError,Unit]]
}

trait Log[F[_]] {
  def info:String => F[Unit]
  def error:String => F[Unit]
}

trait MetricData
case class NoteCounter(title:String,visitCount:Int) extends MetricData
case class TagCounter(tag:String,count:Int) extends MetricData

sealed trait MetricError extends NoteError
case object MetricNotFound extends MetricError

trait Metric[F[_], Title, Tag] {
  def incrementByTitle:Title => F[Either[MetricError,Unit]]
  def incrementByTag:Tag => F[Either[MetricError, Unit]]
  def titleCount:Title => F[Either[MetricError,MetricData]]
  def tagCount:Tag => F[Either[MetricError,MetricData]]
}

trait NoteDsl[F[_]] extends Notes[F,String, String, String] with Log[F] with Metric[F,String, String]
