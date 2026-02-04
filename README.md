# FRC 2026 - Team 3091 REBUILT Robot Code

Competition robot code for the 2026 FRC season built with WPILib's command-based framework.

**Technology Stack:**
- WPILib 2026.2.1
- Java 17
- REV Robotics SparkMax motor controllers (REVLib 2025.0.3)

## Quick Start

**Prerequisites:**
- WPILib 2026 installed ([installation guide](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html))
- VS Code with WPILib extensions
- FRC Driver Station
- PS4 controller

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
- **Type:** 6-motor tank drive (3 motors per side)
- **Motor Controllers:** REV SparkMax (CAN bus)
- **Motors:** NEO Brushless (assumed - adjust in code if using CIMs)

**CAN ID Assignments:**
| ID | Motor Location |
|----|----------------|
| 1  | Left Front     |
| 2  | Left Middle    |
| 3  | Left Back      |
| 4  | Right Front    |
| 5  | Right Middle   |
| 6  | Right Back     |

**Note:** If your robot uses different CAN IDs, update them in `src/main/java/frc/robot/Constants.java` under `DriveConstants`.

### Controller
- **Type:** Sony DualShock 4 (PS4 Controller)
- **Connection:** USB to Driver Station computer
- **Port:** 0

## PS4 Controller Setup

### Connection Instructions

1. Connect PS4 controller via USB cable to Driver Station computer
2. Open FRC Driver Station software
3. Navigate to **USB Devices** tab
4. Verify controller appears on **Port 0**
5. Controller LED should light up when connected

**Note:** USB connection is recommended over Bluetooth for competition reliability.

### Control Layout

```
      Left Joystick                   Right Joystick
         (Y-Axis)                        (Unused)
            │
            │
            ▼
    Forward/Backward Drive
    (Both sides synchronized)


    L2 Trigger                          R2 Trigger
   ───────────                         ───────────
   Add power to                        Add power to
   LEFT side motors                    RIGHT side motors
```

| Control | Function |
|---------|----------|
| **Left Joystick Y** | Forward/Backward drive (both sides synchronized) |
| **L2 Trigger** | Add power to LEFT side motors |
| **R2 Trigger** | Add power to RIGHT side motors |

### Control Examples

The unique trigger + joystick control scheme allows for advanced maneuvering:

**Example 1: Normal Driving**
- Push left joystick forward
- **Result:** Robot drives straight forward (both sides equal speed)

**Example 2: Spin Left in Place**
- Press L2 trigger only (joystick centered)
- **Result:** Left side spins forward, right side stopped → robot turns left

**Example 3: Spin Right in Place**
- Press R2 trigger only (joystick centered)
- **Result:** Right side spins forward, left side stopped → robot turns right

**Example 4: Turn Right While Moving**
- Push left joystick forward
- Press L2 trigger partially
- **Result:** Left side gets extra power → robot curves right while moving forward

**Example 5: Turn Left While Moving**
- Push left joystick forward
- Press R2 trigger partially
- **Result:** Right side gets extra power → robot curves left while moving forward

**Example 6: Differential Drive**
- Press both L2 and R2 triggers at different pressures
- **Result:** Independent control of each side for advanced maneuvering

### Tuning Drive Sensitivity

If the robot feels too sensitive or too slow during practice:

1. Open `src/main/java/frc/robot/Constants.java`
2. Find the `DriveConstants` section
3. Adjust these values:

```java
public static final double kJoystickDeadband = 0.05;  // Increase if joystick drifts
public static final double kTriggerDeadband = 0.05;   // Increase if triggers too sensitive
public static final double kMaxSpeed = 1.0;           // Lower for slower max speed (e.g., 0.7)
public static final double kTriggerScale = 0.8;       // Lower for gentler trigger response
```

4. Rebuild and redeploy: `./gradlew deploy`

## Complete Practice Session Guide

This guide walks you through a complete practice session from powering on the robot to driving with PS4 controllers.

### Step 1: Pre-Practice Hardware Checklist

Before starting, verify all hardware is ready:

- [ ] Battery charged (>12.5V recommended) and securely installed
- [ ] RoboRIO powered on (check for solid status lights)
- [ ] Radio powered and connected (allow 30-45 seconds for boot)
- [ ] All 6 SparkMax controllers powered (check status LEDs)
- [ ] CAN bus terminated properly
- [ ] PS4 controller available with USB cable
- [ ] Laptop ready with FRC Driver Station software

**Verification:** RoboRIO should show solid green communications light after radio boots completely.

### Step 2: Software Prerequisites

**First-time setup only** - skip if already installed:

- WPILib 2026 ([installation guide](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html))
- FRC Driver Station (included with WPILib)
- VS Code with WPILib extensions (included with WPILib)
- Java 17 (included with WPILib)
- REV Hardware Client (optional, for SparkMax configuration)

**Verify Installation:**
- Open VS Code
- Press Ctrl+Shift+P (Windows/Linux) or Cmd+Shift+P (Mac)
- Type "WPILib" - you should see WPILib commands available

### Step 3: Connect to Robot

Choose one of three connection methods:

#### Option A: WiFi (Recommended for Practice)

1. Power on robot and wait 30-45 seconds for radio to boot completely
2. On your laptop, open WiFi settings
3. Connect to WiFi network: **`3091-Robot`** or **`FRC-3091`**
4. Wait for connection to establish (10-15 seconds)
5. Verify connection:
   ```bash
   ping 10.30.91.2
   # OR
   ping roboRIO-3091-FRC.local
   ```

**Verification:** Ping should succeed with replies showing low latency (<50ms).

**Troubleshooting:**
- WiFi not visible: Check radio power LED, wait longer for boot (up to 60 seconds)
- Cannot ping: Check firewall settings, verify correct network
- Slow connection: Move closer to robot

#### Option B: USB (Direct Connection)

1. Connect USB cable from laptop to RoboRIO USB-B port
2. Wait 15-20 seconds for connection to establish
3. No network configuration needed - connection is automatic

**Verification:** RoboRIO appears as network device at `172.22.11.2`.

#### Option C: Ethernet

1. Connect ethernet cable from laptop to robot network switch or RoboRIO
2. Configure laptop with static IP: `10.30.91.5`
3. Subnet mask: `255.255.255.0`
4. Gateway: `10.30.91.1`

**Verification:** Ping `10.30.91.2` succeeds.

### Step 4: Open Project in VS Code

1. Launch **VS Code** with WPILib extensions
2. **File → Open Folder**
3. Navigate to: `/FRC-2026` (or wherever you cloned the repository)
4. Click **Open** and wait for Gradle sync

**First time:** Gradle sync may take 2-3 minutes to download dependencies - be patient!

**Verification:** Bottom status bar shows "WPILib" icon, and `build.gradle` is visible in file explorer.

**Troubleshooting:**
- Gradle sync fails: Check internet connection (needs to download dependencies from Maven)
- Java errors: Verify Java 17 is installed - run `java -version` in terminal
- Project won't open: Ensure you opened the root folder containing `build.gradle`

### Step 5: Build and Deploy Code

Deploy your code to the robot using one of these methods:

**Method 1: Command Palette (Recommended)**
1. Press **Ctrl+Shift+P** (Windows/Linux) or **Cmd+Shift+P** (Mac)
2. Type: `WPILib: Deploy Robot Code`
3. Press Enter and watch the output terminal

**Method 2: Keyboard Shortcut**
- Press **F5** (Deploy and Debug)

**Method 3: Terminal Command**
```bash
./gradlew deploy
```

**Expected Output:**
```
> Task :compileJava
> Task :build
BUILD SUCCESSFUL in 15s

> Task :deploy
Deploying to roboRIO-3091-FRC.local...
Copying to target...
Upload complete
```

Deployment typically takes 20-40 seconds depending on code changes.

**Verification:** Terminal shows "BUILD SUCCESSFUL" and "Upload complete" with no errors.

**Troubleshooting:**
- Build fails with Java errors: Check Problems tab in VS Code for syntax errors
- Cannot connect to robot: Verify Step 3 connection - try pinging the robot
- Timeout during deploy: Try USB connection instead of WiFi for faster deployment
- "Team number not found": Check `.wpilib/wpilib_preferences.json` contains team 3091

### Step 6: Connect PS4 Controller

1. Open **FRC Driver Station** application
2. Connect PS4 controller via USB cable to laptop
3. Controller LED should light up
4. In Driver Station, click the **"USB Devices"** tab
5. Verify controller appears on **Port 0**

**Verification:** Controller shows green status indicator in USB Devices tab, and you can see axis/button values update when you move sticks/press buttons.

**Troubleshooting:**
- Controller not detected: Try different USB port, check cable
- Wrong port number: Disconnect and reconnect - first connected becomes Port 0
- Controller not responding: Update controller firmware via PS4 console

### Step 7: Enable Robot in Driver Station

1. In Driver Station main tab, verify connection status bar at top is **green**
2. Select **"TeleOperated"** mode from the mode selector dropdown
3. Check that all three status indicators are green:
   - **Communications** ✓ (robot connected)
   - **Robot Code** ✓ (code running on robot)
   - **Joysticks** ✓ (controller detected)
4. Click the **"Enable"** button (or press **Space** key)
5. Robot is now active and ready for driver control

**⚠️ SAFETY WARNING:** Ensure robot is on blocks or has clear space to move before enabling! Always have someone ready to hit the emergency stop or disable button.

**Verification:** All three status lights are green, and robot responds to controller input immediately.

**Troubleshooting:**
- "No Robot Code": Code didn't deploy successfully - repeat Step 5
- "No Communications": Lost connection to robot - check Step 3
- Robot immediately disables: Check battery voltage (must be >12V), verify no emergency stop
- Joysticks not detected: Verify controller in USB Devices tab (Step 6)

### Step 8: Test Drive and Systems

Follow this recommended test sequence to verify all systems:

**Basic Drive Tests:**
1. **Forward/Backward:** Gently push left joystick forward → robot drives forward
   - Pull back → robot drives backward
   - Verify both sides spin same direction and speed

2. **L2 Trigger Only:** Press and hold L2 → left side spins, right side stopped
   - Release → motors stop
   - Verify only left motors are running

3. **R2 Trigger Only:** Press and hold R2 → right side spins, left side stopped
   - Release → motors stop
   - Verify only right motors are running

**Advanced Maneuver Tests:**
4. **Turn Right While Moving:** Push joystick forward + press L2 partially
   - Robot should curve right (left side faster than right)

5. **Turn Left While Moving:** Push joystick forward + press R2 partially
   - Robot should curve left (right side faster than left)

6. **Differential Drive:** Press both L2 and R2 at different amounts
   - Experiment with independent side control

**During Testing, Monitor For:**
- Unusual sounds or vibrations from motors
- Motor overheating (check after 2-3 minutes of continuous use)
- Proper response to inputs (no lag or unexpected behavior)
- Any error messages in Driver Station console
- Battery voltage (should stay above 12V during operation)

**Use the "Disable" button (or press Space) immediately if any issues occur!**

**Verification:** All motors respond smoothly to inputs without errors, unusual noises, or overheating.

**If drive feels wrong, see [Tuning Drive Sensitivity](#tuning-drive-sensitivity) section above.**

### Step 9: End Practice Session Safely

When practice is complete, follow this shutdown sequence:

1. Click **"Disable"** button in Driver Station (or press **Space** key)
2. Wait for robot to come to a complete stop (all motors silent)
3. Verify robot is fully stopped before approaching
4. Optionally disconnect PS4 controller (or leave for next session)
5. Close FRC Driver Station application
6. On the robot, flip **main breaker to OFF** position
7. Disconnect and remove **battery**

**Best Practices:**
- **Always disable before approaching the robot**
- **Never power off the robot while enabled** (can damage electronics)
- Store battery on charger if voltage is below 12.5V
- Log any issues encountered during practice for engineering review
- Clean/inspect robot for loose wires or damage
- Check SparkMax LEDs for error codes (blinking patterns)

### Quick Reference Flowchart

For experienced users, here's the complete workflow at a glance:

```
Power On Robot → Wait 45s for Radio → Connect WiFi (3091-Robot)
         ↓
Open VS Code → Open FRC-2026 → Deploy Code (F5)
         ↓
Open Driver Station → Connect PS4 Controller → Verify Port 0
         ↓
Select TeleOp Mode → Check Green Lights → Press Enable (Space)
         ↓
Test: Joystick Forward → Test: L2 Only → Test: R2 Only → Test: Combined
         ↓
Press Disable (Space) → Power Off Robot → Disconnect Battery
```

### Practice Session Troubleshooting

Quick fixes for common issues during practice:

**Robot won't enable:**
- Check battery voltage in Driver Station (must be >12V)
- Verify code deployed successfully (Robot Code indicator green)
- Check Driver Station console for error messages
- Ensure robot is not in emergency stop state
- Try rebooting robot (power cycle)

**Controllers not responding to input:**
- Verify robot is enabled in TeleOp mode (not Disabled/Autonomous)
- Check controller on Port 0 in USB Devices tab
- Test controller by watching axis values update when moving sticks
- Try different USB port or cable

**One or more motors not spinning:**
- Check SparkMax status LEDs (should be solid or slow blink)
- Verify CAN IDs match Constants.java (use REV Hardware Client)
- Check motor power connections (12V to SparkMax)
- Inspect CAN bus wiring and termination

**Robot drives backward when pushing forward:**
- Motor inversions need adjustment in Constants.java
- Flip `kLeftMotorsInverted` or `kRightMotorsInverted`
- Redeploy code and test again

**Robot turns when trying to drive straight:**
- Check that both sides have equal number of motors
- Verify all motors on each side spin same direction
- One side may be inverted incorrectly
- Check for mechanical binding or damage

**Code deployment fails:**
- Verify robot connection: `ping 10.30.91.2`
- Try USB connection instead of WiFi (more reliable)
- Check `build.gradle` for syntax errors
- Ensure no other applications using roboRIO
- Reboot roboRIO if persistent issues

**Robot moves erratically:**
- Joystick deadband may be too low (increase in Constants.java)
- Check for joystick drift (watch axis values when centered)
- Verify triggers return to 0.0 when released
- Check for loose/damaged controller

**For additional help, consult your team's programming lead or review full documentation below.**

## Build & Deploy

**For complete practice session workflow, see [Complete Practice Session Guide](#complete-practice-session-guide) above.**

### Gradle Commands

```bash
# Build the code (compile only, no deploy)
./gradlew build

# Deploy to robot (must be connected)
./gradlew deploy

# Clean build (removes old build files)
./gradlew clean build

# Simulate robot code (no robot required)
./gradlew simulateJava

# Run unit tests
./gradlew test
```

### VS Code Commands

Press **Ctrl+Shift+P** (Windows/Linux) or **Cmd+Shift+P** (Mac) to access:
- **WPILib: Deploy Robot Code** - Build and deploy
- **WPILib: Simulate Robot Code** - Run simulation
- **WPILib: Build Robot Code** - Compile only
- **WPILib: Test Robot Code** - Run tests

## Code Structure

```
src/main/java/frc/robot/
├── Main.java                     # Robot entry point
├── Robot.java                    # TimedRobot base class
├── RobotContainer.java           # Subsystems, controllers, bindings
├── Constants.java                # Configuration constants
├── commands/
│   ├── TeleopDrive.java         # Trigger + joystick drive command
│   └── Autos.java               # Autonomous routines (empty)
└── subsystems/
    └── Drive.java               # 6-motor tank drive subsystem
```

### Key Files

**Constants.java** - All configuration in one place:
- `OperatorConstants`: Controller port (0)
- `DriveConstants`: Motor CAN IDs, inversions, control parameters

**Drive.java** - Drivetrain subsystem:
- 6 CANSparkMax motor controllers
- Tank drive control methods
- Telemetry to SmartDashboard

**TeleopDrive.java** - Default drive command:
- Reads PS4 controller inputs (joystick, L2, R2)
- Applies deadbands
- Mixes inputs additively
- Sends commands to Drive subsystem

**RobotContainer.java** - Robot configuration:
- Instantiates subsystems
- Sets up PS4 controller
- Configures button bindings
- Sets default commands

## Dependencies

- **WPILib 2026.2.1** - FRC robotics framework
- **REVLib 2025.0.3** - REV Robotics SparkMax motor controller library
- **Java 17** - Programming language

**Vendor Dependencies:**
- REVLib - Located in `vendordeps/REVLib.json`

## Troubleshooting

### Motor Issues

**SparkMax not responding:**
- Check CAN ID matches Constants.java
- Verify 12V power to SparkMax
- Use REV Hardware Client to check motor status
- Inspect CAN bus wiring and termination resistor

**Wrong motor direction:**
- Adjust motor inversions in Constants.java:
  ```java
  public static final boolean kLeftMotorsInverted = true;  // Flip if wrong
  public static final boolean kRightMotorsInverted = false;
  ```
- Redeploy and test

**Motors fighting each other:**
- One motor on the same side inverted wrong
- Check individual motor directions using REV Hardware Client
- Update inversions in code

### Controller Issues

**PS4 controller not detected:**
- Check USB cable connection
- Try different USB port on computer
- Update controller firmware via PS4 console
- Verify in Driver Station USB Devices tab

**Buttons/triggers not responding:**
- Controller on wrong port (should be 0)
- Check axis values in Driver Station USB Devices tab
- Deadband too high - lower in Constants.java
- Controller in wrong mode - should be DInput mode

**Joystick drifts:**
- Increase `kJoystickDeadband` in Constants.java
- Clean controller analog sticks
- Calibrate controller if supported

### Deployment Issues

**Build fails:**
- Check Problems tab in VS Code for Java syntax errors
- Run `./gradlew clean build` to clean and rebuild
- Verify Java 17 is installed: `java -version`

**Cannot connect to robot:**
- Verify robot powered on and radio booted
- Check WiFi connection or USB cable
- Ping robot: `ping 10.30.91.2`
- Try different connection method (USB vs WiFi)

**"No Robot Code" in Driver Station:**
- Code failed to deploy - check deployment terminal output
- Redeploy: `./gradlew deploy`
- Reboot roboRIO if persistent

## Competition Checklist

Before competition matches, verify:

- [ ] Battery fully charged (>12.5V)
- [ ] PS4 controller connected and on Port 0
- [ ] All 6 SparkMax controllers powered and responding
- [ ] CAN bus properly terminated
- [ ] Latest code deployed and verified
- [ ] Drive system tested (all motors working)
- [ ] Motor temperatures normal after practice
- [ ] No loose wires or mechanical issues
- [ ] Backup battery charged and available
- [ ] Driver practiced with current control scheme
- [ ] Emergency stop procedures reviewed with team

## Team Information

**Team:** 3091 - REBUILT
**Season:** 2026 FRC Competition
**Contact:** [Your team contact info]

**Robot Specifications:**
- Drive System: 6-motor tank drive
- Motor Controllers: REV SparkMax (CAN)
- Control System: PS4 controller with unique trigger-based drive

**Control Scheme:**
- Left Joystick Y: Synchronized drive (both sides)
- L2 Trigger: Left side power (additive)
- R2 Trigger: Right side power (additive)

---

**Built with ❤️ by Team 3091 using WPILib and the FRC Control System**
