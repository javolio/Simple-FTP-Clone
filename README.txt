Simple-FTP-Clone
================

A console implementation of basic FTP functionality, utilizing TCP/IP. Both downloading and uploading files are supported, as well as directory changes. A single server can handle multiple concurrent clients, and will automatically accept incoming connections. The software is not limited to text files; it can send and receive files of any sort.

On the server, run either "Simple FTP Clone Server.bat" or "Simple FTP Clone Server.sh", depending on your operating system. Alternatively, run "Simple FTP Clone Server.jar" from your command prompt or bash shell.

On each client, run either "Simple FTP Clone Client.bat" or "Simple FTP Clone Client.sh", depending on your operating system. Alternatively, run "Simple FTP Clone Client.jar" from your command prompt or bash shell. Enter the IP Address and Port provided by the server.

Available Commands:
bye - Closes the connection.
cd <directory> - Changes the server's working directory.
get <filename> - Downloads the specified file from the server to the client's working directory.
ls - Lists the files and directories in the working directory of the server.
put <filename> - Uploads the specified file from the client's working directory to the server.
pwd - Prints the working directory of the server.