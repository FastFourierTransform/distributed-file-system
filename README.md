# distributed-file-system
This java application distribute files over several devices

## Architecture

This application have a server side composed of two types of nodes:
 * Master Node
 * Chunk Node

And client side which interact with server side nodes

### Master Node
Offerecs an abstraction of the file system.
Command all chunks nodes.
Client first interact with the Master to find the chunk nodes of the corresponding file.

### Chunk Node
Save parts of a file (chunk) in a certain file system of the running device.
Send and receive the file chunks from the client.

### Protocol communication
```
(byte)messageType | 

messageType
 * 1 - SlaveNode to Master, indicate that SlaveNode is active and what is port and host name using for listning request
 * 2 - Client to Master, send File (long)fileSize|lenght file name|file name
                 Master response nChunks|sizeChunk|fileId|List n->nChunks host slave|port slave
 * 3 - Client to Slave, send chunk - fileId|lenght chunk|bytes chunk
 * 4 - Client to Master, request File file identifier
 * 5 - Client to Slave, request chunk - 
 * ... - Client to Master, command ls
 * ... - Client to Master, command mkdir
```

## Improvements
 * Load/Save the DFS state
 * Bad test methodology
 * Better algoritm/stratagy to divide the file into chunks and distributing it
 * Better usage of the multithread features of send and receive requests from the library
 * Protocol for handling replicas of chunks
 * Possibility of problemens when coverting byte array to String