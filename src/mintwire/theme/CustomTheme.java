
package mintwire.theme;


import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIDefaults;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import mdlaf.themes.AbstractMaterialTheme;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialFontFactory;
import mdlaf.utils.MaterialImageFactory;

public class CustomTheme extends AbstractMaterialTheme {
    public CustomTheme() {
    }

    public void installTheme() {
        this.installColor();
        this.installFonts();
        this.installBorders();
        this.installIcons();
    }

    protected void installFonts() {
        this.fontBold = MaterialFontFactory.getInstance().getFont("BOLD");
        this.fontItalic = MaterialFontFactory.getInstance().getFont("ITALIC");
        this.fontMedium = MaterialFontFactory.getInstance().getFont("MEDIUM");
        this.fontRegular = MaterialFontFactory.getInstance().getFont("REGULAR");
    }

    protected void installIcons() {
        this.selectedCheckBoxIcon = MaterialImageFactory.getInstance().getImage("white/checked_white");
        this.unselectedCheckBoxIcon = MaterialImageFactory.getInstance().getImage("white/unchecked_white");
        this.selectedRadioButtonIcon = MaterialImageFactory.getInstance().getImage("white/radio-checked-white");
        this.unselectedRadioButtonIcon = MaterialImageFactory.getInstance().getImage("white/radio_unchecked-white");
        this.selectedCheckBoxIconTable = MaterialImageFactory.getInstance().getImage("white/checked_white");
        this.unselectedCheckBoxIconTable = MaterialImageFactory.getInstance().getImage("white/unchecked_white");
        this.selectedCheckBoxIconSelectionRowTable = MaterialImageFactory.getInstance().getImage("painted_checked_box");
        this.unselectedCheckBoxIconSelectionRowTable = MaterialImageFactory.getInstance().getImage("unchecked_box");
        this.closedIconTree = MaterialImageFactory.getInstance().getImage("right_arrow");
        this.openIconTree = MaterialImageFactory.getInstance().getImage("down_arrow");
        this.yesCollapsedTaskPane = MaterialImageFactory.getInstance().getImage("yes-collapsed");
        this.noCollapsedTaskPane = MaterialImageFactory.getInstance().getImage("no-collapsed");
        this.warningIconOptionPane = MaterialImageFactory.getInstance().getImage("warning");
        this.errorIconIconOptionPane = MaterialImageFactory.getInstance().getImage("error");
        this.questionIconOptionPane = MaterialImageFactory.getInstance().getImage("question");
        this.informationIconOptionPane = MaterialImageFactory.getInstance().getImage("information");
        this.iconComputerFileChooser = MaterialImageFactory.getInstance().getImage("white/computer");
        this.iconDirectoryFileChooser = MaterialImageFactory.getInstance().getImage("white/folder");
        this.iconFileFileChooser = MaterialImageFactory.getInstance().getImage("white/file");
        this.iconFloppyDriveFileChooser = MaterialImageFactory.getInstance().getImage("white/floppy_drive");
        this.iconHardDriveFileChooser = MaterialImageFactory.getInstance().getImage("white/hard_drive");
        this.iconHomeFileChooser = MaterialImageFactory.getInstance().getImage("white/home");
        this.iconListFileChooser = MaterialImageFactory.getInstance().getImage("white/list");
        this.iconDetailsFileChooser = MaterialImageFactory.getInstance().getImage("white/details");
        this.iconNewFolderFileChooser = MaterialImageFactory.getInstance().getImage("white/new_folder");
        this.iconUpFolderFileChooser = MaterialImageFactory.getInstance().getImage("white/back_arrow");
    }

    protected void installBorders() {
        super.installBorders();
        this.borderMenuBar = new BorderUIResource(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 156, 43)));
        this.borderPopupMenu = new BorderUIResource(BorderFactory.createLineBorder(this.backgroundPrimary, 1));
        this.borderSpinner = new BorderUIResource(BorderFactory.createLineBorder(this.backgroundPrimary, 1));
        this.borderSlider = new BorderUIResource(BorderFactory.createCompoundBorder(this.borderSpinner, BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        this.cellBorderTableHeader = new BorderUIResource(BorderFactory.createCompoundBorder(this.borderSpinner, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.borderToolBar = this.borderSpinner;
        this.borderDialogRootPane = this.borderSpinner;
        this.borderProgressBar = this.borderSpinner;
        this.borderComboBox = MaterialBorders.roundedLineColorBorder(MaterialColors.WHITE, 12);
        this.borderTable = this.borderSpinner;
        this.borderTableHeader = this.borderSpinner;
    }

    protected void installColor() {
        this.backgroundPrimary = new ColorUIResource(45, 48, 56);
        this.highlightBackgroundPrimary = new ColorUIResource(219,80,74);
        this.textColor = new ColorUIResource(255, 255, 255);
        this.disableTextColor = new ColorUIResource(170, 170, 170);
        this.buttonBackgroundColor = new ColorUIResource(45, 48, 56);
        this.buttonBackgroundColorMouseHover = new ColorUIResource(81, 86, 101);
        this.buttonTextColor = MaterialColors.WHITE;
        this.buttonDefaultBackgroundColorMouseHover = new ColorUIResource(219,80,74);
        this.buttonDefaultBackgroundColor = new ColorUIResource(255, 130, 124);
        this.buttonDefaultTextColor = MaterialColors.WHITE;
        this.buttonDisabledBackground = new ColorUIResource(66, 69, 76);
        this.buttonDisabledForeground = MaterialColors.GRAY_500;
        this.buttonFocusColor = MaterialColors.WHITE;
        this.buttonDefaultFocusColor = MaterialColors.BLACK;
        this.buttonBorderColor = new ColorUIResource(255, 130, 124);
        this.buttonColorHighlight = this.buttonBackgroundColorMouseHover;
        this.selectedInDropDownBackgroundComboBox = new ColorUIResource(249, 192, 98);
        this.selectedForegroundComboBox = MaterialColors.BLACK;
        this.menuBackground = this.backgroundPrimary;
        this.menuBackgroundMouseHover = this.buttonBackgroundColorMouseHover;
        this.menuTextColor = MaterialColors.WHITE;
        this.menuDisableBackground = MaterialColors.TRANSPANENT;
        this.arrowButtonBackgroundSpinner = this.buttonBackgroundColor;
        this.mouseHoverButtonColorSpinner = this.buttonBackgroundColorMouseHover;
        this.arrowButtonColorScrollBar = this.buttonBackgroundColor;
        this.trackColorScrollBar = new ColorUIResource(81, 86, 101);
        this.thumbColorScrollBar = new ColorUIResource(155, 155, 155);
        this.thumbDarkShadowColorScrollBar = this.thumbColorScrollBar;
        this.thumbHighlightColorScrollBar = this.thumbColorScrollBar;
        this.thumbShadowColorScrollBar = this.thumbColorScrollBar;
        this.arrowButtonOnClickColorScrollBar = this.buttonBackgroundColorMouseHover;
        this.mouseHoverColorScrollBar = this.thumbColorScrollBar;
        this.trackColorSlider = new ColorUIResource(119, 119, 119);
        this.haloColorSlider = MaterialColors.bleach(new Color(249, 192, 98), 0.2F);
        this.highlightColorTabbedPane = new ColorUIResource(45, 48, 56);
        this.borderHighlightColorTabbedPane = new ColorUIResource(45, 48, 56);
        this.focusColorLineTabbedPane = new ColorUIResource(129,221,108);
        this.disableColorTabTabbedPane = new ColorUIResource(170, 170, 170);
        this.backgroundTable = new ColorUIResource(45, 48, 56);
        this.backgroundTableHeader = new ColorUIResource(57,222,152);
        this.foregroundTable = this.textColor;
        this.foregroundTableHeader = new ColorUIResource(45,48,58);
        this.selectionBackgroundTable = new ColorUIResource(126, 132, 153);
        this.selectionForegroundTable = this.textColor;
        this.gridColorTable = new ColorUIResource(151, 151, 151);
        this.alternateRowBackgroundTable = new ColorUIResource(59, 62, 69);
        this.dockingBackgroundToolBar = MaterialColors.LIGHT_GREEN_A100;
        this.floatingBackgroundToolBar = MaterialColors.GRAY_200;
        this.selectionBackgroundTree = new ColorUIResource(81, 86, 101);
        this.selectionBorderColorTree = this.selectionBackgroundTree;
        this.backgroundTextField = new ColorUIResource(81, 86, 101);
        this.inactiveForegroundTextField = MaterialColors.WHITE;
        this.inactiveBackgroundTextField = new ColorUIResource(81, 86, 101);
        this.selectionBackgroundTextField = new ColorUIResource(219,80,74);
        this.selectionForegroundTextField = MaterialColors.BLACK;
        this.inactiveColorLineTextField = MaterialColors.WHITE;
        this.activeColorLineTextField = new ColorUIResource(219,80,74);
        this.titleBackgroundGradientStartTaskPane = MaterialColors.GRAY_300;
        this.titleBackgroundGradientEndTaskPane = MaterialColors.GRAY_500;
        this.titleOverTaskPane = MaterialColors.WHITE;
        this.specialTitleOverTaskPane = MaterialColors.WHITE;
        this.backgroundTaskPane = this.backgroundPrimary;
        this.borderColorTaskPane = this.backgroundTaskPane;
        this.contentBackgroundTaskPane = this.backgroundPrimary;
        this.selectionBackgroundList = new ColorUIResource(249, 192, 9);
        this.selectionForegroundList = MaterialColors.BLACK;
        this.backgroundProgressBar = new ColorUIResource(81, 86, 101);
        this.foregroundProgressBar = MaterialColors.WHITE;
    }

    public void installUIDefault(UIDefaults table) {
        super.installUIDefault(table);
        table.put("TabbedPane[contentBorder].enableTop", true);
        table.put("TabbedPane[contentBorder].enableLeaf", false);
        table.put("TabbedPane[contentBorder].enableRight", false);
        table.put("TabbedPane[contentBorder].enableBottom", false);
    }

    public String getName() {
        return "Custom";
    }
}
