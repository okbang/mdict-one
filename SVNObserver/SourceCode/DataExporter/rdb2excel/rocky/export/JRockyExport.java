package rocky.export;

public class JRockyExport {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if ((args == null) || (args.length != 4) ) {
            usage();
            System.exit(1);
        } else {
            Rdb2Excel.main(args);
        }
    }
    
    private static void usage() {
        System.out.println("JRockyExport <alias> <ResourceBaseName> <TemplateFilePath> <output file>");
    }
}
