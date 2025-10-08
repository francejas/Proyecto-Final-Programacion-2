# 💻 Proyecto Final Programación 2

Este documento describe cómo iniciar el trabajo y el flujo de desarrollo (Git Flow) que utilizaremos en el proyecto.

---

## 🚀 Inicio

Sigue estos pasos para comenzar a trabajar en el proyecto:

1.  **Clonar el repositorio:**
    ```bash
    git clone LINK_DEL_REPOSITORIO
    cd proyecto-final
    ```

2.  **Cambiar a la rama de desarrollo:**
    Asegúrate de trabajar siempre sobre la rama `develop`.
    ```bash
    git checkout develop
    git pull origin develop
    ```
    ⚠️ **Importante:** Nunca trabajar directamente sobre la rama `main`.

---

## 🔁 Flujo de trabajo (Git Flow)

Utilizamos un flujo de trabajo basado en tres tipos de ramas principales:

| Rama | Propósito |
| :--- | :--- |
| **`main`** | Versión **estable y final** del sistema. |
| **`develop`** | Rama de **integración** (donde se unen todas las *features*). |
| **`feature/nombre-de-la-tarea`** | Rama **temporal** de cada integrante para desarrollar una funcionalidad específica. |

### 🌳 Crear una rama para tu tarea

1.  Actualiza tu rama `develop`:
    ```bash
    git checkout develop
    git pull origin develop
    ```
2.  Crea y cambia a tu nueva rama de *feature*:
    ```bash
    git checkout -b feature/nombre-de-la-tarea
    ```

### 📤 Subir cambios

Una vez que hayas terminado tu trabajo, sube los cambios:
 ```bash
git add .
git commit -m "Agrego [nombre de la funcionalidad]" 
git push origin feature/nombre-de-la-tarea
 ```


## ➡️ Crear un Pull Request (PR)

Una vez subidos tus cambios:

1.  Desde la interfaz de **GitHub**, crea un **Pull Request (PR)** desde tu rama `feature/...` hacia la rama **`develop`**.
2.  Un compañero o el responsable del proyecto revisará tu código (*code review*) antes de hacer el *merge*.

---

## 📋 Project Board (Gestión de Tareas)

Todas las tareas del proyecto se administran en el tablero de **GitHub Projects**:\

### 🚥 Columnas del tablero:

| Símbolo | Columna | Estado |
| :---: | :--- | :--- |
| 🟡 | **Pendiente / To do** | Tareas sin comenzar. |
| 🔵 | **En progreso / In progress** | Tareas en desarrollo. |
| 🟢 | **Finalizado / Done** | Tareas completadas o *mergeadas*. |

⚙️ El tablero se actualiza **automáticamente** cuando se crean o cierran *issues* y *Pull Requests*.

---

## ✅ Buenas prácticas

Mantén estas prácticas al trabajar con **Git**:

* ✅ **Siempre** crear ramas desde **`develop`**.
* ✅ Mensajes de *commit* **claros y en presente** (“Agrego”, “Corrijo”, “Refactorizo”, etc.).
* ✅ No subir archivos innecesarios (`pycache`, `venv`, etc.).
* ✅ Revisar el archivo **`.gitignore`** antes de hacer `git add .`.
* ✅ Hacer `git pull` antes de empezar a trabajar cada día para asegurar tener la última versión de la rama.
