# Chunk Viewer Tool

## Overview

**Chunk Viewer Tool** is a lightweight and interactive 3D application for visualizing and experimenting with chunk generation in a custom voxel engine. The tool allows users to explore procedurally generated chunks in real time, with plans to add a graphical interface for dynamic parameter editing.

![V1 Preview](https://github.com/floeme/chunk_viewer_tool/blob/main/images/visuel.jpg)

---

## Features (v1.1)

- ✅ Real-time chunk generation
- ✅ First-person camera to navigate the 3D world
- ✅ Block rendering with basic lighting and textures
- ✅ Multithreaded chunk population system
- ✅ Custom voxel engine with OpenGL (LWJGL)

---

## Roadmap (v1.2+)

Next version will include:

- 🔜 Graphical User Interface (GUI) using ImGui or a custom panel
- 🔜 Real-time chunk parameter editing (e.g. size, heightmap function)
- 🔜 Visualization of generation steps and performance metrics
- 🔜 In-world interaction (block editing, debug tools)

---

## Technologies

- **Java 20**
- **LWJGL 3** (OpenGL, GLFW, stb)
- **Custom ECS-like architecture**
- **Multithreaded chunk generation**
- **Matrix and vector math implementation**

---

## Project Structure

```
src/
├── engine/
│   ├── graphics/     # Shader, Mesh, Renderer, Material
│   ├── io/           # Window, Input
│   ├── maths/        # Vector/Matrix classes
│   └── objects/      # Entity, Block, Chunk, Camera
├── utils/            # File loader
└── Main.java         # Entry point
```

---

## Running the Project

Make sure you have:

- Java 20+ installed
- LWJGL libraries set up (or use Maven for dependencies)

Then compile and run:

```bash
javac -sourcepath src -d out src/fr/florian/Main.java
java -cp out fr.florian.Main
```

---

## License

This project is for educational and experimental purposes. No specific license applies yet.

---

## Author

Made by [@floeme](https://github.com/floeme)
