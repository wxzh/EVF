package fullerror;

import fullerror.tyalg.shared.TyAlgDefault;
import utils.IMeet;

public interface Meet<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IMeet<Ty>>, bot.Meet<Ty> {
}