The server component is a simple Java app that communicates with Julius using the server module interface of the engine. It basically looks more or less like this:

  1. phone app sends speech to Julius engine on port A
  1. server receives output of Julius on port B
  1. optionally, phone app receives output from server on port C

The task of the server is therefore the following:
  * parse the output from the ASR engine
  * match the output with appropriate rule
  * run the appropriate command based on the output