# Tendable Automated Test

## How to Run the Test

1. Ensure you have Java and Maven installed.
2. Clone the repository.
3. Navigate to the project directory.
4. Open Project in Eclipse
5. Make sure that all dependencies are downloaded into .m2 local repository
6. Right click on Project
7. Select Run As and Select Maven Test from the submenu

## Strategy

1. **Top-Level Menus**: Check if each menu item is accessible by ensuring it is both displayed and enabled.
2. **Request a Demo Button**: For each top-level menu page, verify the presence and activeness of the "Request a Demo" button.
3. **Contact Us Form**: Attempt to submit the form without filling out the "Message" field to verify the presence of an error message.
4. **Bug Report**: If the error message is not displayed, a bug report will be generated and saved.
