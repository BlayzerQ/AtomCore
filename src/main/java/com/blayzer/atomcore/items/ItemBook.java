package com.blayzer.atomcore.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.category.CategoryResourceLocation;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

@GuideBook
public class ItemBook implements IGuideBook {

    public static Book myGuide;

    @Nonnull
    @Override
    public Book buildBook() {
    	
        // Setup the list of categories and add our entries to it.
        List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
    	
        // Create the map of entries. A LinkedHashMap is used to retain the order of entries.
        Map<ResourceLocation, EntryAbstract> entries1 = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        Map<ResourceLocation, EntryAbstract> entries2 = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        Map<ResourceLocation, EntryAbstract> entries3 = new LinkedHashMap<ResourceLocation, EntryAbstract>();

        categories.add(new CategoryItemStack(entries1, "История", new ItemStack(Blocks.MYCELIUM)));
        categories.add(new CategoryItemStack(entries2, "Выживание", new ItemStack(Items.COOKED_RABBIT)));
        categories.add(new CategoryItemStack(entries3, "Защита территории", new ItemStack(com.blayzer.atomcore.block.Blocks.blockRain)));
        //--------------------------------------------------------------------------------------------------------------------------------------
        
        // Creation of our first entry.
        List<IPage> pages_riceofend = new ArrayList<IPage>();
        List<IPage> pages_newera = new ArrayList<IPage>();
        
        entries1.put(new ResourceLocation("example", "entry_riceofend"), new EntryItemStack(pages_riceofend, "Начало конца", new ItemStack(Blocks.DIRT)));
        entries1.put(new ResourceLocation("example", "pages_newera"), new EntryItemStack(pages_newera, "Новое начало", new ItemStack(Blocks.GRASS)));
        
        pages_riceofend.add(new PageText("1982 год. Череда затяжных конфликтов СССР и стран запада, развязавших ядерную войну уничтожила все культурное наследие, что строилось и развивалось веками. Многие города были разрушены взрывами бомб, дома были разграблены."));
        pages_riceofend.add(new PageText("Все что не сгорело и не умерло в пучинах ядерного пожара добило изменение климата. Множественные взрывы ядерных ракет породили кардинальные изменения климата на планете Земля. Серый, плотный смог, висел над землей почти год,"));
        pages_riceofend.add(new PageText("не пропуская солнечные лучи на землю. Ночь, которую многие не пережили тогда, закончилась спустя десять лет, лучи наконец-то достигли поверхности земли. Сильнейший холод сменился адским жаром - солнце в следствии истончения"));
        pages_riceofend.add(new PageText("озонового слоя уничтожило почти все живое на земле и высушив большую часть воды на планете, которая затем проливалась над землей в виде кислотных дождей, добивая растительность. Почти вся местность превратилась в выжженную пустыню - пустоши."));
        pages_riceofend.add(new PageText("В то время, все кто успел - укрылись в бункеры расположенные по всей планете. Немногие из них были достаточно оборудованы чтобы долгое время укрывать людей. Им приходилось выходить на поверхность слишком рано, и немногие из них выживали."));
        pages_riceofend.add(new PageText("Говорят, что те кто был вынужден выходить на поверхность, лишались человеческого облика. И не только из-за множественных кожных мутацией, но и адаптации под новую, агрессивную среду - им приходилось есть крыс и пить грязную, зараженную воду."));
        pages_riceofend.add(new PageText("Убивать и есть людей, тех кто был слабее. Они одичали и потеряли свою человечность. Это уже не люди. Некоторые из них до сих пор обитают там, на поверхности, лишенные человеческого облика. В разрушенных городах, зараженных радиацией,"));
        pages_riceofend.add(new PageText("в забытых поселениях и даже просто бродящие по миру. Если встретите их, то знайте - это уже не люди. Убивайте их без угрызений совести или они убьют вас. В пустоши опасность повсюду - ловушки, радиация, кислотные дожди, агрессивные существа -"));
        pages_riceofend.add(new PageText("все что осталось от некогда могущественной цивилизации, все это может поколечить или убить, поэтому если вам когда нибудь придется выйти в пустоши - будьте осторожны и удачи - она вам понадобится."));
        
        pages_newera.add(new PageText("Прошло уже достаточно много времени. Наш бункер один из немногих что смог продержать людей настолько долго, чтобы основной враг - радиация, уже не была для вас самым опасным врагом в новом, жестоком, мире."));
        pages_newera.add(new PageText("Коалиция - так их называют. Новое государство, если хотите - общество тех кому было не все равно. Благодаря им был организован новый миропорядок, восстановлена структура бункеров и связь между ними. Каждый из них - бункеров,"));
        pages_newera.add(new PageText("которые смогли продержать людей настолько долго, это их заслуга. Заслуга Коалиции. Но припасы не бесконечны и даже с самым умелым управлением невозможно бесконечно снабжать и содержать людей, рано или поздно это должно было случится."));
        pages_newera.add(new PageText("И это случилось. Настало время выпускать людей в новый в свет, выпускать вас в новый свет. Настало время возрождать цивилизацию из пепла и разрухи. Коалиация все еще несет за вас ответственность, поэтому она снабдит вас"));
        pages_newera.add(new PageText("всем необходимым, чтобы вы, если вам повезет, не умерли на поверхности слишком быстро. Добро пожаловать, в новый, по своему прекрасный мир - мир хаоса и разрушений, который вам придется возродить из пепла."));

        // -------------------------------------------------------------------------------------------------------------------------------------
        
        // Creation of our second entry.
        List<IPage> pages_world = new ArrayList<IPage>();
        List<IPage> pages_bioms = new ArrayList<IPage>();
        List<IPage> entry_struct = new ArrayList<IPage>();
        List<IPage> entry_surv = new ArrayList<IPage>();
        List<IPage> entry_rads = new ArrayList<IPage>();
        List<IPage> entry_mobs = new ArrayList<IPage>();
        
        entries2.put(new ResourceLocation("example", "entry_world"), new EntryItemStack(pages_world, "О мире", new ItemStack(Blocks.TALLGRASS)));
        entries2.put(new ResourceLocation("example", "entry_biomes"), new EntryItemStack(pages_bioms, "Биомы", new ItemStack(Blocks.SAND)));
        entries2.put(new ResourceLocation("example", "entry_struct"), new EntryItemStack(entry_struct, "Структуры", new ItemStack(Blocks.OAK_FENCE)));
        entries2.put(new ResourceLocation("example", "entry_surv"), new EntryItemStack(entry_surv, "Выживание", new ItemStack(Items.PORKCHOP)));
        entries2.put(new ResourceLocation("example", "entry_rads"), new EntryItemStack(entry_rads, "Радиация", new ItemStack(Blocks.VINE)));
        entries2.put(new ResourceLocation("example", "entry_mobs"), new EntryItemStack(entry_mobs, "Мобы", new ItemStack(Items.SPAWN_EGG)));
        
        pages_world.add(new PageText("Мир в котором вам предстоит выживать очень опасен. Весь мир пронизах радиацией и где-то ее больше, а где-то меньше. На ее уровень в мире и на отдельных участках карты, техногенным образом, может влиять игрок."));
        pages_world.add(new PageText("Наибольшее количество радиации сконцентрировано в Городах и биомах Xeric Shrubland, Radiation Island. Помимо радиации естественным образом вам может представлять опасность кислотный дождь. От дождя можно "));
        pages_world.add(new PageText("укрыться под любыми блоками или используя защитный костюм. В пустынных биомах во время дождя по миру, может начинаться бесчанная буря - от нее потребуется укрываться в пещерах, плотно обустроенных домах или надева распиратор. "));
        pages_world.add(new PageText("Также, в пустынных биомах вы можете вступить в зыбучие пески, они тоже довольно опасны - обходите их стороной. В мире вы можете встретить много зданий, во многих из них вас будут поджидать ловушки и другие опасности."));
        pages_world.add(new PageText("А основным вашим врагом, конечно же будут враждебно настроенные существа - мутанты, мобы, и конечно же, игроки. Не каждый игрок в этом жестоком мире будет играть роль хорошего и мирного выходца из убежища, помните об этом."));
        
        pages_bioms.add(new PageText("Весь мир, в основном, состоит из биома Wasteland - это сухой, безжизненный биом, в котором нет почти ничего полезного. Все руды разбросаны по разным биомам, каждому биому соответствует свой тип руды и ископаемого."));
        pages_bioms.add(new PageText("Список основных ресурсов в биомах: \n §8Wasteland: Уголь (Coal) \n §0Bog: Боксит (Bauxite) и Янтарь (Amber) \n §8Desert: Редстоун (Redstone) и Апатит (Apatite) \n §0Volcanic Island: Алмаз (Diamond) и x2 Уголь (Coal)"));
        pages_bioms.add(new PageText("§8Mesa: Золото (Gold) и Уголь (Coal) \n §0Outback: Медь (Copper) и Золото (Gold) \n §8Cold desert: Олово (Tin), Снег (Snow) и Перидот (Peridot) \n §0Lush Desert: Редстоун (Redstone) и Рубин (Ruby) \n §8Cold desert: Олово (Tin), Снег (Snow) и Перидот (Peridot)"));
        pages_bioms.add(new PageText("§8Brushland: Лазурит (Lapis), Изумруд (Emerald), Дерево и Животные. \n §0Steppe: Железо (Iron) и Топаз (Topaz) \n §8Shrubland: Никель (Nickel), Танзанит (Tanzanite), Дерево и Животные."));
        pages_bioms.add(new PageText("§8Tundra: Серебро (Silver), Галена (Galena), Дерево и Животные. \n §0Xeric Shrubland (Радиоктивен): Иридий (Iridium) и Осмий (Osmium) \n §8Radiation Island (Omnik) (Радиоктивен): Уран (Uranium), Торий (Thorium)."));
        pages_bioms.add(new PageText("§8Dead Swamp: Свинец (Lead), Борон (Boron) и Гивеи. \n §0Quagmire: Литий (Lithium), Малахит (Malachite) и Гивеи \n §8SKelp Forest: Осмий (Osmium), Сапфир (Saphire), Глина (Clay)."));
        
        entry_struct.add(new PageText("Путешествуя по миру вы можете встретить 3 типа структур: §8Одиночные здания (Руины или небольшие домики, в них редко можно встретить что нибудь полезное); §0Поселения (Группа одиночных домиков, иногда в них есть жители и можно встретить полезный лут;"));
        entry_struct.add(new PageText("§8Города (Достаточно редкие, заражены высоким уровнем радиации, здания населяют монстры и в них можно найти очень ценный лут). Посещать их рекомендуется только подготовившись (Приняв препараты от радиации, надев защитный костюм и т.д.)."));
        entry_struct.add(new PageText("§0К одиночным структурам так же относятся небольшие ловушки, сооруженные скитальцами пустошей и источники пресной воды (Воды в таких источниках обычно много и уровень радиации которая она дает немного меньше)."));
        
        entry_surv.add(new PageText("В игре на вас влияют несколько параметров, в первую очередь это конечно голод - восполнить еду вы можете как обычно, но животные в пустошах редкость (Нужно искать в биомах), а значит вам придется импровизировать - например, есть сырую плоть. "));
        entry_surv.add(new PageText("Также немного еды может восполнить кактусовая плоть (Cactus Flesh), а еще она восполняет и воду - второй показатель, который влияет на игрока. В основном воду можно добыть из любого источника наполнив ее во флягу или бутылку, "));
        entry_surv.add(new PageText("но, такая вода будет грязнай и если ее не очистить, то некоторое время вы будете испытывать жажду и поднимите свой уровень радиации. Вода можно очистить с помошью специальных фильтров или собрать воду собирателем дождя."));
        entry_surv.add(new PageText("Вода в мире не бесконечна, бесконечные источники воды теперь доступны только в виде технологических преимушеств (Резервуары из модов), поэтому при выборе места для жилья учитывайте наличие водоемов наподалеку."));
        entry_surv.add(new PageText("Когда начинается дождь, старайтесь укрыться где нибудь, если у вас нет защитного костюма. Дождь только на первый взгляд кажется безопасным. Аналогично и буре в пустыне. Но не рекомендуем вообще находится в ней в такие моменты."));
        entry_surv.add(new PageText("Не путешествуйте по ночам, ночью мобы ведут себя гораздно агрессивнее и могут навредить куда сильнее, особенно не стоит этого делать если у вас нет должной экипировки чтобы вы могли себя защитить."));
        
        entry_rads.add(new PageText("Весь мир пронизан радиацией, ее уровень и ее влияние на вас вы можете узнавать с помощью счетчика гейгера. Он издает характерный звук и добавляет показатель на экран. Старайтесь не допускать критического уровня радиации, это вас убьет."));
        entry_rads.add(new PageText("В основном, по миру небольшой радиоктивный фон, но в некоторых биомах и городах он может быть очень высоким - обязательно следите за показателями радиации и вовремя принимайте медикаменты - RadX снижает влияние радиации на вас в течение 10 минут, "));
        entry_rads.add(new PageText("А Антирадин (RadAWay) снижает ваш уровень радиации. Не забывайте и о защитных костюмах - они лучшее средство от радиации. Вы также можете встраивать защитные пластины в любую вашу броню, но это потребует некоторых усилий."));
        entry_rads.add(new PageText("Чем выше уровень радиации на вас, тем более серьезные симптомы вы будете испытывать, не допускайте повышения уровня радиации. По достижению критического уровня радиации вы умрете."));
        entry_rads.add(new PageText("Многие изотопы и радиоктивные блоки имеют уровень радиации, они непосредственно будут влиять на вас. При работе с реакторами не допускайте их взрыва и нарушения герметичности, чтобы не повышать радиацию в чанке."));
        entry_rads.add(new PageText("Радиация каждые несколько тиков будет распространяться по ближайшим чанкам, если вы допустили ее скопление в одном из ваших чанков. Вы можете использовать очистной блок, который будет абсорбировать радиацию в чанке и потреблять энергию."));
        entry_rads.add(new PageText("Также каждые несколько тиков будет проходить полураспад радиоактивных частиц в чанке, но такой способ крайне не эффективен и сгодится только в случае небольших загрязнений."));
        entry_rads.add(new PageText("С помощью блоков очистки вы можете очистить от радиации целый город, и организовать там поселение или индустриальный центр, но для этого потребуются усилия многих игроков. В одиночку реализовать такое будет крайне сложно."));
        
        entry_mobs.add(new PageText("В мире вы можете встретить опасных мутантов и различного рода существ, и некоторые из них крайне опасны. Ночью мобы ведут себя агрессивнее, поэтому ночью не рекомендуется путешествовать."));
        entry_mobs.add(new PageText("Зомби сбиваются в стаи, некоторые из них бегают очень быстро и имеют снаряжения лучше вашего - учитывайте это, если решитесь на открытую схватку с ними. Многие существа будут помогать друг другу, они как и люди - стараются быть в стае."));
        
        //----------------------------------------------------------------------------------------------------------------------------------------
        
        // Creation of our three entry.
        List<IPage> pages_claimstory = new ArrayList<IPage>();
        List<IPage> pages_claim = new ArrayList<IPage>();
        
        entries3.put(new ResourceLocation("example", "pages_claimstory"), new EntryItemStack(pages_claimstory, "Коалиция и защита", new ItemStack(Blocks.BARRIER)));
        entries3.put(new ResourceLocation("example", "pages_claim"), new EntryItemStack(pages_claim, "Создание защищенной зоны", new ItemStack(Blocks.IRON_BARS)));
        
        pages_claimstory.add(new PageText("Технологическое развитие не стояло на месте все эти годы, под руководством Коалиции была создана специальная система защиты, которая призвана защищать выходцев убежища от непрошенных гостей и недружественных людей."));
        pages_claimstory.add(new PageText("Для поддерживания такой огромной системы требуется много энергии, а так же на обеспечение государственных нужд, поэтому в замен на защиту, Коалиция, предоставляя доступ к глобальной системе защиты ГСЗ,"));
        pages_claimstory.add(new PageText("требует от вас поставлять ей определенный процент энергии, который ваша задача будет добывать. Таким образом каждый будет помогать каждому, все будут зависимы друг от друга и работать на благо всеобщего процветания."));
        pages_claimstory.add(new PageText("Каждому выходцу из убежища, Коалиция дает доступ к чертежам технологии ГСЗ, и обязывает поставлять на ее обеспечение определенное количество энергии. В комплект ГСЗ входят два инструмента: "));
        pages_claimstory.add(new PageText("Координатный инструмент, который позволяет выделить координаты площади в мире, которую обязана будет зищащать Коалиция и отправляет данные в штаб."));
        pages_claimstory.add(new PageItemStack("Координатный инструмент", "coordTool"));
        pages_claimstory.add(new PageText("Контроллер привата, который является центром управления вашего участка ГСЗ и приемщиком энергии для обеспечения защиты. Коалиция будет защищать вашу территорию до тех пор, пока вы поставляете ей энергию."));
        pages_claimstory.add(new PageItemStack("Контроллер привата", com.blayzer.atomcore.block.Blocks.blockRain));
        
        pages_claim.add(new PageText("Для начала вам необходимо создать Координатный инструмент и Контроллер привата, затем держа в руке Координатный инструмент, с помощью кнопок мыши ЛКМ и ПКМ установить две точки - выделить кубоид. Вы будете видеть выделенный регион."));
        pages_claim.add(new PageText("Затем, в выделенный регион вам необходимо установить блок Контроллер привата, который создаст регион после своей установки. После установки регион будет доступен только тому кто устанавливал блок."));
        pages_claim.add(new PageText("Для добавления человека в свой приват, вы должны находять в регионе ввести команду /rp addmember <ник>, а командой /rp removemember <ник> вы сможете удалить человека из вашей территории."));
        pages_claim.add(new PageText("Чтобы удалить регион необходимо сломать Контроллер привата, вместе с этим удалиться и очистится защищенная зона. На каждой вашей территории можно установить только один такой Контроллер."));
        pages_claim.add(new PageText("После создания защищенной зоны она не будет сразу активной, вам нужно будет подать на нее необходимое количество энергии и запустить процесс перезапуска, нажав кнопку в инвентаре блока. Количество энергии зависит от размера региона."));
        pages_claim.add(new PageText("Если вы не активируете защиту или если вы не будете питать данный блок энергией, кто угодно сможет ломать/ставить блоки в ней, открывать и изменять что угодно. Не пренебрегайте этим и внимательно следите за энергией в блоке."));
        
        //------------------------------------------------------------------------------------------------------------------------------------------

        // Setup the book's base information
        myGuide = new Book();
        myGuide.setTitle("Руководство выжевшего");
        myGuide.setDisplayName("Руководство выжевшего");
        myGuide.setAuthor("Введение в выживание. 1986 год.");
        myGuide.setColor(Color.CYAN);
        myGuide.setCategoryList(categories);
        myGuide.setRegistryName(new ResourceLocation("atomcore", "guide"));
        return myGuide;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleModel(ItemStack bookStack) {
        // Use the default GuideAPI model 
        GuideAPI.setModel(myGuide);
    }

    @Override
    public void handlePost(ItemStack bookStack) {
        // Register a recipe so player's can obtain the book
        //GameRegistry.addShapelessRecipe(bookStack, Items.BOOK, Items.PAPER);
    }
}
