object CommandEnum extends Enumeration:
  type Type = CommandEnumVal
  val Help = Value("HELP", true)
  val Exit = Value("EXIT", false)
  val Current = Value("CURRENT", true)
  val Forecast = Value("FORECAST", true)
  val Astronomy = Value("ASTRONOMY", true)
  val Timezone = Value("TIMEZONE", true)
  val Football = Value("FOOTBALL", true)
  case class CommandEnumVal(value: String, continue: Boolean) extends Val(nextId, value)
  final protected def Value(value: String, continue: Boolean) = new CommandEnumVal(value, continue)

  def isCommand(word: String): Boolean =
    List(Help, Exit, Current, Forecast, Astronomy, Timezone, Football).map(_.value).contains(word)
