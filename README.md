# IngSW2024- Maule-Maiorana-Masi-Marino
<h1 align="center">
  <br>
  <a href="https://www.craniocreations.it/prodotto/codex-naturalis">
    <img src="https://studiobombyx.com/assets/Slider-Codex-2-1920x1080.jpg" alt="CodexNaturalis"></a>
  Project for ing-sw
  <br>
  PoliMi
  
</h1>

<h4 align="center">A version of the game `CodexNaturalis`  made by: <br><br>
TEAM: <br>
<a href="https://github.com/sofiamaule" target="_blank">Maule Sofia</a><br>
<a href="https://github.com/martinamaiorana" target="_blank">Maiorana Martina</a><br>
<a href="https://github.com/masiirene" target="_blank">Masi Irene Pia</a> <br>
<a href="https://github.com/margheritamarino" target="_blank">Marino Margherita</a></h4>


## Progress:
We have `implemented`, in addiction to the `Game Specific` and `Game Agnostic` requirements,  the following advanced features: <br>
| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Complete rules | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Socket | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| RMI | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| GUI | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| CLI | ![#c5f015](https://placehold.it/15/44bb44/44bb44)|
| Chat | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Persistence | ![#c5f015](https://placehold.it/15/f03c15/f03c15) |
| Multiple Games | ![#c5f015](https://placehold.it/15/f03c15/f03c15) |
| Disconnections | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |

<br>

**Cross platform**
- Windows and macOS ready.

# Setup:
- In the [Deliverables](deliverables) folder there are two multi-platform jar files, one to set the Server up, and the other one to start the Client.
- The Server can be run with the following command for windows, linux, linux-aarch64 and mac intel x86, the RMI port is 4321, the socket port is 4320:
    ```shell
    > java -jar serverMain.jar
    ```
- For mac-aarch64 the server can be run with the following command:
 ```shell
    > java -jar serverMainx64.jar
  ```
- The Client can be run with the following command for windows, linux, linux-aarch64 and mac intel x86:
    ```shell
    > java -jar clientMain.jar
    ```
- For mac-aarch64 the client can be run with the following command:
   ```shell
    > java -jar clientMainx64.jar
    ```


## Credits

This software uses the following open source packages:

- [Json-Simple](https://code.google.com/archive/p/json-simple/)
- [OpenJfx](https://openjfx.io/)
- [JUnit](https://junit.org/junit5/)
- [Jansi](https://fusesource.github.io/jansi/)

## License
CodexNaturalis is property of CranioCreations and all of the copyrighted graphical assets used in this project were supplied by Politecnico di Milano.