
package mintwire;


public class ExtensionSelector {

    public static String select(String s) {
        String res;
        switch (s) {

            case "text/plain":
                res =".txt";
                break;
            case "text/actionscript":
                res = ".as";
                break;
            case "text/asm":
                res = ".s";
                break;
            case "text/bbcode":
                res =".txt" ;
                break;
            case "text/c":
                res =".c";
                break;
            case "text/clojure":
                res = ".clj";
                break;
            case "text/cpp":
                res =".cpp";
                break;
            case "CSHARP":
                res = "text/cs";
                break;
            case "text/css":
                res = ".cs";
                break;
            case  "text/d":
                res =".d";
                break;
            case "text/dockerfile":
                res = ".dockerfile";
                break;
            case "text/dart":
                res =".dart" ;
                break;
            case "text/delphi":
                res = ".dpr";
                break;
            case "text/dtd":
                res = ".dtd";
                break;
            case "text/fortran":
                res =".f";
                break;
            case "text/groovy":
                res = ".groovy";
                break;
            case "text/hosts":
                res = ".txt";
            case "text/htaccess":
                res = ".htaccess";
                break;
            case "text/html":
                res = ".html";
                break;
            case "text/ini":
                res =".ini" ;
                break;
            case "text/java":
                res = ".java";
                break;
            case "text/javascript":
                res =".js" ;
                break;
            case "text/json":
                res = ".json";
                break;
            case "text/jshintrc":
                res = ".jshintrc";
                break;
            case "text/jsp":
                res = ".jsp";
                break;
            case "text/latex":
                res = ".tex";
                break;
            case "text/less":
                res =".less";
                break;
            case "text/lisp":
                res = ".lisp";
                break;
            case "text/lua":
                res = ".lua";
                break;
            case "text/makefile":
                res =".mk" ;
                break;
            case "text/mxml":
                res = ".mxml";
                break;
            case "text/nsis":
                res =".nsi" ;
                break;
            case "text/perl":
                res = ".pl";
                break;
            case "text/php":
                res = ".php";
                break;
            case "text/properties":
                res = ".properties";
                break;
            case "text/python":
                res =".py" ;
                break;
            case "text/ruby":
                res = ".ruby";
                break;
            case "text/sas":
                res = ".sas";
                break;
            case "text/scala":
                res = ".scala";
                break;
            case "text/sql":
                res = ".sql";
                break;
            case "text/tcl":
                res = ".tcl";
                break;
            case "text/typescript":
                res =".ts" ;
                break;
            case "text/unix":
                res = ".sh";
                break;
            case "text/vb":
                res = ".vb";
                break;
            case "text/bat":
                res = ".bat";
                break;
            case "text/xml":
                res = ".xml";
                break;
            case "text/yaml":
                res =".yaml";
                break;
            default:
                res = ".txt";
        }
        return res;
    }

}
