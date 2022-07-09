import api.ResponseHandler
import parsing.JsonToObjectParser
import service.{Console, Service}
import table.{TableManager, TableVisualizer}

import scala.io.StdIn.readLine

object Main:

  val service: Service = new Service(new TableManager(new JsonToObjectParser), new TableVisualizer, new ResponseHandler)
  val console: Console = new Console(service)

  def main(args: Array[String]): Unit =
    println("Please enter 'help' in order to see detailed information about the supported operations.")
    while console.continue(readLine().toUpperCase()) do println("")
