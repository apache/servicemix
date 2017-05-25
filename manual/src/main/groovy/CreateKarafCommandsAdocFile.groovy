def dir = new File(project.properties.karafCommandsDir)
def out = new File(project.properties.karafCommandsDir,"karaf-commands.adoc")

out.write ("= Karaf Commands\n\n")

def commands = [:]
dir.eachFile { it ->  
    def line = it.readLines()[0]
    def command = line - ~/^=\s/
    def filename = command.replace(":","-") + ".adoc"
    def link = "<<${filename}#,${command}>>"
    commands.put(command, link)
}

def keys = commands.keySet().sort()
def lastGroup = ""
keys.findAll { it.contains(":") }.each { key ->
    def group = key.split(":")[0]
    if (!lastGroup.equals(group)) {
        lastGroup = group
        out.append("\n== ${group}\n\n")
    }
    def link = commands[key]
    out.append("* ${link}\n")
}
