import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Service:
  def help(args: List[String]): Future[Unit] = Future.successful(
    Table.plotTable(
      List(
        List("command", "description"),
        List("help", "This command prints out this information."),
        List("exit", "This command stops the programme."),
        List("current <city>", "This command shows detailed information about the weather in the chosen city(<city>)."),
        List(
          "forecast <city>",
          "This command shows the forecast for the next day for the chosen city(<city). It also plots the temperature."
        ),
        List(
          "astronomy <city> <date>",
          "This command shows astronomy details about the chosen city(<city>) on the chosen date(<date>). If date is not entered, the date is today by default."
        ),
        List("timezone <city>", "This command prints out information about the timezone of the chosen city(<city>)."),
        List("football <city>", "This command prints out information about the football matches for the day.")
      )
    )
  )

  def current(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains symbols."))
    else
      ResponseHandler
        .createParsedCurrent(CommandEnum.Current.value.toLowerCase, commandArguments)
        .map(value =>
          val args = value.parsedValue.myArgs
          Table.plotTable(List(args.map(_._1), args.map(_._2)))
        )

  def forecast(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains symbols."))
    else
      ResponseHandler
        .createParsedForecast(CommandEnum.Forecast.value.toLowerCase, commandArguments)
        .map(value =>
          if value.parsedValue.isEmpty then println("There is no forecast.")
          else
            val args = value.parsedValue(0).myArgs
            Table.plotTable(args.map(_._1) :: value.parsedValue.map(_.myArgs.map(_._2)))
        )

  def astronomy(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains symbols."))
    else
      ResponseHandler
        .createParsedAstronomy(CommandEnum.Astronomy.value.toLowerCase, commandArguments)
        .map(value =>
          val args = value.parsedValue.myArgs
          Table.plotTable(List(args.map(_._1), args.map(_._2)))
        )

  def timezone(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains symbols."))
    else
      ResponseHandler
        .createParsedTimeZone(CommandEnum.Timezone.value.toLowerCase, commandArguments)
        .map(value =>
          val args = value.parsedValue.myArgs
          Table.plotTable(List(args.map(_._1), args.map(_._2)))
        )

  def football(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains symbols."))
    else
      ResponseHandler
        .createParsedFootball(CommandEnum.Football.value.toLowerCase, commandArguments)
        .map(value =>
          if value.parsedValue.isEmpty then println("There are no matches.")
          else
            val args = value.parsedValue(0).myArgs
            Table.plotTable(args.map(_._1) :: value.parsedValue.map(_.myArgs.map(_._2)))
        )

  def checkArgs(args: List[String], executioner: List[String] => Future[Unit]): Future[Unit] =
    if args.filter(CommandEnum.isCommand(_)).size != 0 then
      Future { throw new IllegalStateException("You can enter only one command per time!") }
    else executioner(args)
