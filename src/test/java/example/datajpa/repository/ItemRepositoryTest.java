package example.datajpa.repository;

import example.datajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Persistable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemRepositoryTest(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Test
    public void save(){

        // JPA save 메서드는 Id값(pk)이 null이면 persist(새로운 엔티티 판단), !null 이면 merge(기존 엔티티 판단) 메서드를 사용한다.
        // 사실 특수한 상황이 아니고선 merge 메서드를 사용할일이 거의 없으므로, 데이터 변경은 변경감지 + persist 메서드로 가야한다.
        // @GeneratedValue 사용 : pk 필드멤버 변수(id)를 엔티티에서 초기화하지 않아도 됨 (save 시점 id값은 null) --> 새로운 엔티티로 인식!
        // @Id만 사용하고 생성자로 직접 할당할 경우 : 새로운 엔티티이지만 값이 null 또는 0이 아님, 기존 엔티티라고 오판단함.
        //      --> 해결방법은 Persistable 인터페이스 상속 후 isNew 구현 메서드에서 새로운 엔티티 판단 로직 구현!
        Item item = new Item("A");
        itemRepository.save(item);

    }
}