# FRC 2026 - Team 3091 REBUILT Robot Code

Great Season Everyone. I hope everyone had fun, and is wanting to come back for another season of FRC with the 100SRA in 2027. Im leaving this codebase up for next year, and whoever wants to look at the robot code. Please take your time reading through this README.md file. This should be a very detailed skeleton for whomever may be touching the code.

Reminder... This is in Java just because, i'd challenge you to try and run this through python, you will be the first person in our program history to run the robot with python code... big achievement. 

Please have fun, this is just a stepping stone of the big engineer you will be in the future. 

Good luck.

-Julius L. Jones Jr. (programming coach)


# Robot Code Structure 

**Technology Stack:**
- WPILib 2026.2.1
- Java 17
- REV Robotics SPARK MAX motor controllers (REVLib 2026.0.1)
- PhotonVision v2026.2.1 (vision targeting)

## Quick Start

**Prerequisites:**
- WPILib 2026 installed ([installation guide](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html))
- VS Code with WPILib extensions
- FRC Driver Station
- 2x PS4 controllers (driver + operator)

**Build and Deploy:**
```bash
# Build the code
./gradlew build

# Deploy to robot (must be connected)
./gradlew deploy

# Simulate robot code
./gradlew simulateJava
```

## Hardware Configuration

### Drivetrain
- **Type:** 4-motor tank drive (2 motors per side)
- **Motor Controllers:** PWM Spark

| PWM Port | Motor |
|----------|-------|
| 0 | Left Drive Motor 1 |
| 1 | Left Drive Motor 2 |
| 2 | Right Drive Motor 1 |
| 3 | Right Drive Motor 2 |

### Mechanisms

| Port / ID | Subsystem | Controller Type |
|-----------|-----------|-----------------|
| PWM 4 | Loader | Spark (PWM) |
| PWM 5 | Shooter | Spark (PWM) |
| PWM 8 | Collector Arm | Spark (PWM) |
| CAN 3 | Collector | REV SPARK MAX |

> **Climb is currently disabled.** The mechanism exceeds legal size limits.
> CAN IDs 6 and 7 (formerly climb motors) are unused. To re-enable, uncomment
> the climb blocks in `Climb.java`, `RunClimb.java`, `Constants.java`, `Autos.java`,
> `RobotContainer.java`, and `Robot.java`.

### Vision
- **Library:** PhotonVision v2026.2.1
- **Camera:** Configure the camera name in `Vision.java` → `CAMERA_NAME`
  - Open `http://photonvision.local:5800` to find the exact name (case-sensitive)
- **Dashboard telemetry:** `Vision/HasTarget`, `Vision/DistanceMeters`, `Vision/ShooterSpeed`

## Autonomous Routines

Select the desired routine from the **Auto Chooser** widget on the Driver Station dashboard before the match.

| Routine | Description | Default? |
|---------|-------------|----------|
| **Collect and Score** | Deploy arm → drive out + collect (3s) → retract arm → drive back → shoot (5s) | ✅ Yes |
| **Drive and Shoot** | Drive forward 2s → shoot for 10s at 70% power | No |
| **Vision Drive and Shoot** | Drive forward until target visible → shoot at vision-calculated speed | No |

**Collect and Score timing (13.5s total):**
1. Deploy collector arm — 1.5s
2. Drive forward + collect simultaneously — 3.0s
3. Retract arm — 1.0s
4. Drive backward to return — 3.0s
5. Shoot — 5.0s

## Control Layout

### Driver Controller — Port 0 (PS4)

| Input | Function |
|-------|----------|
| **Left Joystick Y** | Left side motors (tank drive) |
| **Right Joystick Y** | Right side motors (tank drive) |
| **R2** (hold) | Both drive motors forward |
| **L2** (hold) | Both drive motors backward |
| **R1** | *(disabled — was climb up)* |
| **L1** | *(disabled — was climb down)* |

> All drive inputs blend together — you can combine R2/L2 with the joysticks simultaneously.

### Operator Controller — Port 1 (PS4)

| Input | Function |
|-------|----------|
| **R2** (analog, hold) | Shooter — speed proportional to trigger pressure |
| **L2** (analog, hold) | Collector — speed proportional to trigger pressure |
| **L1** (hold) | Collector — reverse (eject) |
| **Circle** (hold) | Loader — fixed speed (feed, requires shooter at speed first) |
| **X** (hold) | Shooter — 10% preset (close range) |
| **D-pad Up** (hold) | Collector arm — retract (wind string) |
| **D-pad Down** (hold) | Collector arm — extend (unwind string) |
| **D-pad Left** (hold) | Shooter — 40% preset |
| **D-pad Right** (hold) | Shooter — 70% preset |

> **Shooter interlock:** The loader (Circle) will only activate once the shooter has been spinning at or above 50% power for at least 0.75 seconds. This prevents jamming from firing before the shooter is at speed.

### Tuning Drive Sensitivity

Adjust these values in `Constants.java` under `DriveConstants`:

```java
public static final double kJoystickDeadband = 0.01; // Increase if joystick drifts
public static final double kTriggerDeadband  = 0.01; // Increase if triggers too sensitive
public static final double kMaxSpeed         = 1.0;  // Lower for slower max speed (e.g. 0.7)
public static final double kTriggerScale     = 0.8;  // Lower for gentler trigger response
public static final double kDriveSlewRate    = 3.0;  // Lower for smoother acceleration
```

## Code Structure

```
src/main/java/frc/robot/
├── Main.java                        # Robot entry point
├── Robot.java                       # TimedRobot base class
├── RobotContainer.java              # Subsystems, controllers, bindings, auto chooser
├── Constants.java                   # All configuration constants
├── commands/
│   ├── Autos.java                   # Autonomous routines (collectAndScore, driveAndShoot, visionDriveAndShoot)
│   ├── TeleopDrive.java             # R2/L2/joystick tank drive command
│   ├── RunCollector.java            # Hold-to-run collector command (L2 analog)
│   ├── RunCollectorArm.java         # Hold-to-run collector arm command (D-pad Up/Down)
│   ├── RunLoader.java               # Hold-to-run loader command (Circle)
│   ├── RunShooter.java              # Hold-to-run shooter command (R2 analog)
│   └── RunClimb.java                # [DISABLED] Climb command — uncomment to re-enable
└── subsystems/
    ├── Drive.java                   # 4-motor PWM tank drive
    ├── Collector.java               # Collector intake (CAN SPARK MAX)
    ├── CollectorArm.java            # Collector arm string mechanism (PWM, with extend ramp)
    ├── Loader.java                  # Loader/feeder (PWM)
    ├── Shooter.java                 # Shooter with spin-up interlock (PWM)
    ├── Vision.java                  # PhotonVision target detection + distance/speed estimation
    └── Climb.java                   # [DISABLED] Climb subsystem — uncomment to re-enable
```

## Troubleshooting

**Robot drives sideways or rotates instead of going straight:**
- Verify motor inversions in `DriveConstants` — flip `kLeftMotorsInverted` or `kRightMotorsInverted`
- Make sure R2/L2 are fully released when using joysticks

**One side of drive not moving:**
- Check PWM cable connections on RoboRIO ports 0–3
- Verify Spark controller status LEDs

**Mechanism motor not responding:**
- Loader: check PWM port 4
- Shooter: check PWM port 5
- Collector Arm: check PWM port 8
- Collector: open REV Hardware Client and confirm SPARK MAX is on CAN ID 3

**Wrong motor direction:**
- Flip the relevant `kInverted` constant in `Constants.java` and redeploy

**Loader won't run (Circle button does nothing):**
- The shooter interlock requires shooter to be spinning at ≥50% for 0.75s first
- Hold R2 or a shooter preset first, then press Circle

**Joystick drift:**
- Increase `kJoystickDeadband` in `Constants.java`

**Vision not detecting targets:**
- Confirm camera name in `Vision.java` → `CAMERA_NAME` matches exactly what PhotonVision shows at `http://photonvision.local:5800`
- Check that the PhotonVision coprocessor is powered and connected to the robot network

**Build fails:**
- Run `./gradlew clean build`
- Check the Problems tab in VS Code for Java errors

**"No Robot Code" in Driver Station:**
- Redeploy: `./gradlew deploy`
- Power cycle the RoboRIO if it persists

### Connect to Robot

**Option A: WiFi (recommended for practice)**
1. Power on robot and wait 30–45 seconds for radio to boot
2. Connect laptop WiFi to **`3091-Robot`** or **`FRC-3091`**
3. Verify: `ping 10.30.91.2`

**Option B: USB (most reliable for deployment)**
1. Connect USB cable from laptop to RoboRIO USB-B port
2. Connection is automatic — no configuration needed

**Option C: Ethernet**
- Static IP: `10.30.91.5`, Subnet: `255.255.255.0`, Gateway: `10.30.91.1`

### Deploy Code

**VS Code:** Press `Ctrl+Shift+P` → `WPILib: Deploy Robot Code`

**Terminal:**
```bash
./gradlew deploy
```

Expected output ends with `BUILD SUCCESSFUL` and `Upload complete`.

### Connect Controllers

1. Open **FRC Driver Station**
2. Plug in **driver PS4 controller** via USB → verify it appears on **Port 0**
3. Plug in **operator PS4 controller** via USB → verify it appears on **Port 1**
4. In Driver Station **USB Devices** tab, confirm both controllers show green status

### Enable Robot

1. Verify all three status indicators are green: **Communications**, **Robot Code**, **Joysticks**
2. Select the desired mode (Autonomous or TeleOperated)
3. For autonomous: confirm **Auto Chooser** is set to the correct routine on the dashboard
4. Click **Enable** (or press **Space**)

> ⚠️ **Safety:** Ensure the robot has clear space to move before enabling. Always have someone ready to disable.



## Dependencies

- **WPILib 2026.2.1** — FRC robotics framework
- **REVLib 2026.0.1** — REV Robotics SPARK MAX (`com.revrobotics.spark.SparkMax`)
- **PhotonVision v2026.2.1** — Vision targeting and distance estimation
- **Java 17** — Programming language

## Team Information

**Team:** 3091 — REBUILT
**Season:** 2026 FRC Competition

---

**Built with ❤️ by Team 3091 using WPILib and the FRC Control System**
