appender("syserr", ConsoleAppender) {
  target = "System.err"
  encoder(PatternLayoutEncoder) {
    pattern = "%level %logger - %msg%n"
  }
}
root(DEBUG, ["syserr"])