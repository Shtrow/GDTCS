# +------------------------------+
# | Good Duck Transfert Protocol |
# | Version: 0.1                 |
# | RFC: draft-5                 |
# | Authors: Marais-Viau         |
# +------------------------------+

VERSION="0.1"

all: compile_client compile_server

version:
	@printf "Compiler for version %s.\n" $(VERSION)

compile_common:
	@printf "Compile extern library.\n"
	javac -cp src src/common/*.java

compile_server: compile_common
	@printf "Compile the server.\n"
	javac -cp src src/server/*.java

compile_client: compile_common
	@printf "Compile the client.\n"
	javac -cp src src/client/*.java

server: compile_server
	@printf "Run server on port 1027.\n"
	java -cp src server.Server

client: compile_client
	@printf "Run client.\n"
	java -cp src client.Client

clean_common:
	@printf "Clean_common.\n"
	rm -rf src/common/*.class

clean_client:
	@printf "Clean client.\n"
	rm -rf src/client/*.class

clean_server:
	@printf "Clean server. \n"
	rm -rf src/server/*.class

clean: clean_client clean_server clean_common


