The Sea Glass Look and Feel is a Java pluggable Look and Feel for
JRE versions 1.6 and later.

See LICENSE.txt for license information.

Some info on the properties used by Synth:

http://download.oracle.com/javase/1.5.0/docs/api/javax/swing/plaf/synth/doc-files/componentProperties.html

Some client properties that are used in Seaglass:
---------------------
Seaglass.Overrides = If the component has overrides, it gets its own unique style instead of the shared style.
SeaGlass.Overrides.InheritDefaults = true to merge with the UI defaults false to replace them.
Seaglass.State = Force the state of the component to the given value example: "Enabled+MouseOver"
JComponent.sizeVariant = the size variant of the component (See seaglass project page for details)
Slider.paintThumbArrowShape = Always force a thumb arrow for the JSlider
__arrow_scale__ = The arrow size for arrow buttons. (Used internally)
JButton.buttonType = Button type : (See seaglass project page for details)
JButton.segmentPosition = Position for segmented buttons. (See seaglass project page for details)
SeaGlass.JRootPane.MenuInTitle = Put the menu into the title area of the main frame. (See seaglass project page for details)
SeaGlass.UnifiedToolbarLook = unified look for the title area the menu and the toolbar. (See seaglass project page for details)
JTabbedPane.Tab.segmentPosition = Used for the JTabbedPane to set the tab segment mode.
JTabbedPane.closeButton = Activate the close button for all tabs of a JTabbedPane (See seaglass project page for details)
JTabbedPane.closeListener = Install a close listener on the JTabbedPane close buttons (See seaglass project page for details)
SeaGlass.JTextArea.drawLineSeparator = Draw separator lines in the JTextArea.
SeaGlass.Override.ScrollBarButtonsTogether = Draw both scroll buttons together (See seaglass project page for details)
JTextField.variant = The variant of the text field: "search" to make it a search field. (See seaglass project page for details)
JTextField.Search.FindPopup = The popup menu for the searh field. (See seaglass project page for details)
JTextField.Search.PlaceholderText = A place holder / hint text for the search field. (See seaglass project page for details)
JTextField.Search.FindAction = An action listener for the find button in the search field. (See seaglass project page for details)
JTextField.Search.CancelAction = An action listener for the cancel button in the search field. (See seaglass project page for details)
Table.sortOrder = Used for the table header renderer to render the correct sort indicator.
Window.documentModified = Draw a document modified indicator on OSX only. (See seaglass project page for details)

System property:
SeaGlass.BorderFactory.overrideDefaults = Deactivate the border factory override. 












