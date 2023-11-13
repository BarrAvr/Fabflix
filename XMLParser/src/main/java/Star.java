public class Star {

    private final String id;

    private final String name;

    private final int birthYear;

    public Star(String id, String name, int birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getId() {
        return id;
    }

    public String getName() { return name; }

    public int getBirthYear() { return birthYear; }

    public String toString() {

        return "ID:" + getId() + ", " +
                "name:" + getName() +
                "Birth Year:" + getBirthYear() +  ".";
    }

    public String toSQLInsertString() {
        return String.format("INSERT INTO stars (id, name, birthYear) VALUES ('%s', '%s', %s)", getId(), getName(), getBirthYear());
    }
}
