import scala.io.StdIn.readLine

object Main:

  val console: Console = new Console(new Service)

  def main(args: Array[String]): Unit =
    println("Please enter 'help' in order to see detailed information about the supported operations.")
  while console.continue(readLine().toUpperCase()) do println("")
