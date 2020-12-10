# mintwire

### 🍀mintwire was my bachelor thesis, a love letter to Peer-to-Peer networks, motivated by my desire to destigmatize them.

Written in Java, it has a decentralized, structured and "pure" P2P network architecture.

> Decentralized - completely server-less architecture, where every node can connect to another node or adhere to a network.

>Structured - it uses a routing table to efficiently send data.

>"Pure" - none of its nodes do any extra work, as established by hybrid networks.

### To exemplify its full potential, using the P2P architecture is an app that makes sharing resources between programmers a lot easier ✨.
So, a mintnode joining the network can be deconstructed into an instance of the P2P substrate (I've used FreePastry), and applications that run on top of it, dictating it's four primary functions:

![mintnode structure](/assets/images/mintnode.png "Mintnode Structure")

## Functionalities

1. Code Stitch - The center of the app, it allows users to send "code stitches", code snippets written in any of the 49 languages supported using the integrated syntax highlighting text editor provided by RSyntaxTextArea. If  either C, CSS, HTML, JSP, Java, JavaScript, PHP or XML are chosen, it has the ability to auto complete or mark errors in the code. This feature can be turned off in the Preferences, keeping in mind that it's designed especially for code snippets, and not full code files. Code Stitches can be sent to multiple available peers.

 📷Screenshots:

 ![mintnode structure](/assets/images/codestitch3.png "Code Stitch editor")
  ![mintnode structure](/assets/images/codestitch4.png "Sending a stitch to available peers")

   ![mintnode structure](/assets/images/codestitch5.png "Getting a stitch request")
    ![mintnode structure](/assets/images/codestitch6.png "Checking available peers")

2. Code Stitch Party - Similar to Code Stitch, but it's designed for viewing a peer's live coding session. It uses FreePastry Scribe, and a publish and subscribe paradigm. The starting node, also called the "root" starts a topic (generating a code) and publishes the code at a given time rate, while subscribers get notified of it. You can unsubscribe from the topic, both as a subscriber and as a publishing node, in which case you will send a terminal message to your subscribers, unsubscribing them.

 📷Screenshots:

 ![mintnode structure](/assets/images/codestitch7.png "Writing code live for a peer")

  ![mintnode structure](/assets/images/codestitch8.png "Creating a party")
  ![mintnode structure](/assets/images/codestitch9.png "Entering a party")

3. File Spore - it allows for a peer to view shared resources from other available peers. You can assign any folder on your PC to share with your peers, view, filter and request other shared resources. Requested items have to be approved from the owner before sending.

 📷Screenshots:

 ![mintnode structure](/assets/images/filespore1.png "Shared resources showed as a table")

  ![mintnode structure](/assets/images/filespore2.png "Accepting or denying a file transfer")

4. Mint Lynx - a simple in-app chat. Chat bubbles wrap to the text accordingly.

 📷Screenshots:

  ![mintnode structure](/assets/images/mintlynx10.png "A Mint Lynx chat")

 ## 📦Key Libraries used:

 BCrypt, for hashing passwords.

 Caffeine, for the Mint Lynx message cache system.

 RSyntaxTextArea, for the markup editors.

 SQLite, as a local database for users and passwords.
 
