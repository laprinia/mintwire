package mintwire;

public class LangSelector {

    public static String select(String s) {
        String res;
        switch (s) {

            case "NONE":
                res = "text/plain";
                break;
            case "ACTIONSCRIPT":
                res = "text/actionscript";
                break;
            case "ASSEMBLER_X86":
                res = "text/asm";
                break;
            case "BBCODE":
                res = "text/bbcode";
                break;
            case "C":
                res = "text/c";
                break;
            case "CLOJURE":
                res = "text/clojure";
                break;
            case "CPLUSPLUS":
                res = "text/cpp";
                break;
            case "CSHARP":
                res = "text/cs";
                break;
            case "CSS":
                res = "text/css";
                break;
            case "D ":
                res = "text/d";
                break;
            case "DOCKERFILE":
                res = "text/dockerfile";
                break;
            case "DART":
                res = "text/dart";
                break;
            case "DELPHI":
                res = "text/delphi";
                break;
            case "DTD":
                res = "text/dtd";
                break;
            case "FORTRAN":
                res = "text/fortran";
                break;
            case "GROOVY":
                res = "text/groovy";
                break;
            case "HOSTS":
                res = "text/hosts";
            case "HTACCESS":
                res = "text/htaccess";
                break;
            case "HTML":
                res = "text/html";
                break;
            case "INI":
                res = "text/ini";
                break;
            case "JAVA":
                res = "text/java";
                break;
            case "JAVASCRIPT":
                res = "text/javascript";
                break;
            case "JSON":
                res = "text/json";
                break;
            case "WITH_COMMENTS":
                res = "text/jshintrc";
                break;
            case "JSP":
                res = "text/jsp";
                break;
            case "LATEX":
                res = "text/latex";
                break;
            case "LESS":
                res = "text/less";
                break;
            case "LISP":
                res = "text/lisp";
                break;
            case "LUA":
                res = "text/lua";
                break;
            case "MAKEFILE":
                res = "text/makefile";
                break;
            case "MXML":
                res = "text/mxml";
                break;
            case "NSIS":
                res = "text/nsis";
                break;
            case "PERL":
                res = "text/perl";
                break;
            case "PHP":
                res = "text/php";
                break;
            case "PROPERTIES_FILE":
                res = "text/properties";
                break;
            case "PYTHON":
                res = "text/python";
                break;
            case "RUBY":
                res = "text/ruby";
                break;
            case "SAS":
                res = "text/sas";
                break;
            case "SCALA":
                res = "text/scala";
                break;
            case "SQL":
                res = "text/sql";
                break;
            case "TCL":
                res = "text/tcl";
                break;
            case "TYPESCRIPT":
                res = "text/typescript";
                break;
            case "UNIX_SHELL":
                res = "text/unix";
                break;
            case "VISUAL_BASIC":
                res = "text/vb";
                break;
            case "WINDOWS_BATCH":
                res = "text/bat";
                break;
            case "XML":
                res = "text/xml";
                break;
            case "YAML":
                res = "text/yaml";
                break;
            default:
                res = "text/plain";
        }
        return res;
    }

}
