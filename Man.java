package Denis;

import java.util.Collections;
import java.util.List;

public class Man {
    private String name;
    private int age;
    private List<String> favoriteBooks;

    public Man(String name, int age, List<String> favoriteBooks) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }




    public static void main(String[] args) {
        Man man = new Man("Denis", 36, Collections.singletonList("Зелёный шум"));
        Man copy = CopyUtils.deepCopy(man);
        copy.setName("LapDenSer");
        copy.setFavoriteBooks(Collections.singletonList("Эффективное программирование"));
        copy.setAge(37);

        System.out.println(man.getName());
        System.out.println(copy.getName());
        System.out.println(man.getFavoriteBooks());
        System.out.println(copy.getFavoriteBooks());
        System.out.println(man.getAge());
        System.out.println(copy.getAge());

    }
}
