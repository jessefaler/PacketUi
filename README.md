# PacketUI

**PacketUI** is a packet-based user interface API for **Bukkit** and **Velocity** servers. It allows developers to create dynamic, interactive GUIs that are rendered client-side by sending packets directly through the server's Netty pipeline.

Because PacketUI operates entirely at the packet level, it is **platform-independent**, lightweight, and does not rely on the server's native inventory or UI systems.

## Features

- **Cross-platform**: Works on both Bukkit and Velocity with no code changes.
- **Low-level performance**: Utilizes [PacketEvents](https://github.com/retrooper/packetevents) to send packets efficiently.
- **Dynamic content**: Easily create and update GUI elements in real time.
- **Customizable**: Full control over layout, appearance, and behavior.

---
