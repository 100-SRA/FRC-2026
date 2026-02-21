# FRC 2026 - Team 3091 REBUILT Robot Code

Competition robot code for the 2026 FRC season built with WPILib's command-based framework.

**Technology Stack:**
- WPILib 2026.2.1
- Java 17
- REV Robotics SPARK MAX motor controllers (REVLib 2026.0.1)

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

**PWM Port Assignments:**
| Port | Motor |
|------|-------|
| 0 | Left Drive Motor 1 |
| 1 | Left Drive Motor 2 |
| 2 | Right Drive Motor 1 |
| 3 | Right Drive Motor 2 |

### Mechanisms
| Port / ID | Motor | Controller Type |
|-----------|-------|-----------------|
| PWM 4 | Loader | Spark (PWM) |
| PWM 5 | Shooter | Spark (PWM) |
| CAN 3 | Collector | REV SPARK MAX |

## Control Layout

### Driver Controller — Port 0 (PS4)

| Input | Function |
|-------|----------|
| **R2** (hold) | Both drive motors forward |
| **L2** (hold) | Both drive motors backward |
| **Right Joystick Y** | Forward / backward (arcade drive) |
| **Right Joystick X** | Turn left / right (arcade drive) |

> All inputs blend together — you can combine R2/L2 with the joystick simultaneously.

### Operator Controller — Port 1 (PS4)

| Input | Function |
|-------|----------|
| **R1** (hold) | Run collector — CAN ID 3, SPARK MAX |
| **L1** (hold) | Run loader — PWM port 4 |
| **Cross / X** (hold) | Run shooter — PWM port 5 |

> All mechanism buttons are hold-to-run — releasing the button stops the motor immediately.

### Tuning Drive Sensitivity

If the robot feels too sensitive or too slow, adjust these values in `Constants.java` under `DriveConstants`:

```java
public static final double kJoystickDeadband = 0.05; // Increase if joystick drifts
public static final double kTriggerDeadband  = 0.05; // Increase if triggers too sensitive
public static final double kMaxSpeed         = 1.0;  // Lower for slower max speed (e.g. 0.7)
public static final double kTriggerScale     = 0.8;  // Lower for gentler trigger response
```

Rebuild and redeploy after any changes: `./gradlew deploy`

## Practice Session Guide

### Step 1: Pre-Practice Hardware Checklist

- [ ] Battery charged (>12.5V) and securely installed
- [ ] RoboRIO powered on (solid status lights)
- [ ] Radio powered and connected (allow 30–45 seconds to boot)
- [ ] All motor controllers powered (check status LEDs)
- [ ] CAN bus terminated properly
- [ ] Both PS4 controllers available with USB cables
- [ ] Laptop with FRC Driver Station software ready

### Step 2: Connect to Robot

**Option A: WiFi (recommended for practice)**
1. Power on robot and wait 30–45 seconds for radio to boot
2. Connect laptop WiFi to **`3091-Robot`** or **`FRC-3091`**
3. Verify: `ping 10.30.91.2`

**Option B: USB (most reliable for deployment)**
1. Connect USB cable from laptop to RoboRIO USB-B port
2. Connection is automatic — no configuration needed

**Option C: Ethernet**
- Static IP: `10.30.91.5`, Subnet: `255.255.255.0`, Gateway: `10.30.91.1`

### Step 3: Deploy Code

**VS Code:** Press `Ctrl+Shift+P` → `WPILib: Deploy Robot Code`

**Terminal:**
```bash
./gradlew deploy
```

Expected output ends with `BUILD SUCCESSFUL` and `Upload complete`.

### Step 4: Connect Controllers

1. Open **FRC Driver Station**
2. Plug in **driver PS4 controller** via USB → verify it appears on **Port 0**
3. Plug in **operator PS4 controller** via USB → verify it appears on **Port 1**
4. In Driver Station **USB Devices** tab, confirm both controllers show green status

### Step 5: Enable Robot

1. Verify all three status indicators are green: **Communications**, **Robot Code**, **Joysticks**
2. Select **TeleOperated** mode
3. Click **Enable** (or press **Space**)

> ⚠️ **Safety:** Ensure the robot has clear space to move before enabling. Always have someone ready to disable.

### Step 6: Test Sequence

**Drive tests:**
1. Push right joystick forward → robot drives straight forward
2. Push right joystick right → robot turns right
3. Hold R2 only → both sides spin forward, robot moves forward
4. Hold L2 only → both sides spin backward, robot moves backward

**Mechanism tests:**
1. Hold operator **R1** → collector motor runs; release → stops
2. Hold operator **L1** → loader motor runs; release → stops
3. Hold operator **Cross** → shooter motor runs; release → stops

### Step 7: End Session Safely

1. Click **Disable** (or press **Space**)
2. Wait for robot to fully stop
3. Flip main breaker to **OFF**
4. Remove and store battery

## Code Structure

```
src/main/java/frc/robot/
├── Main.java                        # Robot entry point
├── Robot.java                       # TimedRobot base class
├── RobotContainer.java              # Subsystems, controllers, bindings
├── Constants.java                   # All configuration constants
├── commands/
│   ├── TeleopDrive.java             # R2/L2/joystick drive command
│   ├── RunCollector.java            # Hold-to-run collector command
│   ├── RunLoader.java               # Hold-to-run loader command
│   ├── RunShooter.java              # Hold-to-run shooter command
│   └── Autos.java                   # Autonomous routines (placeholder)
└── subsystems/
    ├── Drive.java                   # 4-motor tank drive subsystem
    ├── Collector.java               # Collector (CAN SPARK MAX)
    ├── Loader.java                  # Loader (PWM)
    └── Shooter.java                 # Shooter (PWM)
```

## Troubleshooting

**Robot drives sideways or rotates instead of going straight:**
- Check that R2/L2 are fully released when using the joystick
- Verify motor inversions in `DriveConstants` — flip `kLeftMotorsInverted` or `kRightMotorsInverted`

**One side of drive not moving:**
- Check PWM cable connections on RoboRIO ports 0–3
- Verify Spark controller status LEDs

**Mechanism motor not responding:**
- Loader/Shooter: Check PWM cable in ports 4 or 5
- Collector: Open REV Hardware Client and confirm SPARK MAX is on CAN ID 3

**Wrong motor direction:**
- Flip the relevant `kInverted` constant in `Constants.java` and redeploy

**Joystick drift:**
- Increase `kJoystickDeadband` in `Constants.java`

**Build fails:**
- Run `./gradlew clean build`
- Check the Problems tab in VS Code for Java errors

**"No Robot Code" in Driver Station:**
- Redeploy: `./gradlew deploy`
- Power cycle the RoboRIO if it persists

## Competition Checklist

- [ ] Battery fully charged (>12.5V)
- [ ] Driver controller on Port 0, operator controller on Port 1
- [ ] All motor controllers powered and responding
- [ ] CAN bus properly terminated
- [ ] Latest code deployed and verified
- [ ] All drive motors working (both sides, correct direction)
- [ ] All mechanism motors tested (collector, loader, shooter)
- [ ] Motor temperatures normal after practice
- [ ] No loose wires or mechanical issues
- [ ] Driver and operator practiced with current control scheme
- [ ] Emergency stop procedures reviewed with team

## Dependencies

- **WPILib 2026.2.1** — FRC robotics framework
- **REVLib 2026.0.1** — REV Robotics SPARK MAX library (`com.revrobotics.spark.SparkMax`)
- **Java 17** — Programming language

## Team Information

**Team:** 3091 — REBUILT
**Season:** 2026 FRC Competition

---

**Built with ❤️ by Team 3091 using WPILib and the FRC Control System**
