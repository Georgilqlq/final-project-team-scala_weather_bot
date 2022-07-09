import Utils.NO_HISTORY_MESSAGE

class TableVisualizer:
  def visualizeSheet(sheet: List[List[String]], converter: List[String] => List[List[Object]]): Unit =
    if sheet.isEmpty
    then println(NO_HISTORY_MESSAGE)
    else
      val arg: List[List[Object]] = converter(sheet(0))
      Table.plotTable(arg ++ sheet.tail.map(converter).flatMap(_.tail))

  def visualizeRow(row: List[String], converter: List[String] => List[List[Object]]): Unit =
    Table.plotTable(converter(row))
