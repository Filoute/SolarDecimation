package unnaincompris.LunaZ.Manager.Language.commands;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.Editable;
import unnaincompris.LunaZ.Manager.Language.Language;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.MultipleInventory.Items;
import unnaincompris.LunaZ.Manager.MultipleInventory.MultipleInventory;
import unnaincompris.LunaZ.utils.*;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.InteractiveAsk.BooleanAsk;
import unnaincompris.LunaZ.utils.InteractiveAsk.StringAsk;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class EditCommand extends SubCommand implements Listener {
    public EditCommand() {
        super("edit", Config.SERVER_NAME + ".Language.Edit", true);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private MultipleInventory mainLanguageEditor;
    private MultipleInventory languageEditor;
    private Language lang = LanguageManager.getLanguage();

    private final HashMap<String, MultipleInventory> langCategories = new HashMap<>();
    private final Items editLangItem = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {}
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.ARROW, "&aCreate &7/ &6Set &7/ &cDelete", 1, null, null);
        }
    }));
    private final Items backItem = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {}
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.RED_WOOL, lang.BACK, 1, null, null);
        }
    }));
    private final Items createLanguageItem = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {}
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.GREEN_WOOL, "&aCreate new language", 1, null, null);
        }
    }));

    private final Items allLangItems = new Items(() -> {
        lang = LanguageManager.getLanguage();
        langCategories.clear();
        List<Items.ActionItem> items = new ArrayList<>();
        try {
            final HashMap<Annotation, List<Field>> categoriesList = new HashMap<>();
            for (Field field : lang.getClass().getDeclaredFields()) {
                for(Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof Editable || annotation instanceof Expose) continue;
                    if(!categoriesList.containsKey(annotation)) categoriesList.put(annotation, new ArrayList<>());
                    categoriesList.get(annotation).add(field);
                }
            }

            for(Annotation key : categoriesList.keySet()) {
                String keyAsString = key.toString().replace("@unnaincompris.LunaZ.Manager.Language.Categories.", "").replace("()", "");
                items.add(new Items.ActionItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {}

                    @Override
                    public ItemStack getItem() {
                        return ItemUtils.createItem(Material.BOOK, "&7" + keyAsString, 1, null, null);
                    }
                });
                MultipleInventory newInv = new MultipleInventory(new Items(() -> {
                    final List<Items.ActionItem> x = new ArrayList<>();
                    try {
                        for (Field field : categoriesList.get(key)) {
                            List<String> lore = new ArrayList<>();
                            lore.addAll(StringUtils.prettySplit(field.get(lang).toString(), true, 120));

                            Language defaultLang = new Language();
                            lore.add(LanguageManager.getLanguage().DEFAULT);
                            lore.addAll(StringUtils.prettySplit(defaultLang.getClass().getField(field.getName()).get(defaultLang).toString(), true, 120));
                            List<String> placeHolder = StringUtils.findPlaceholders(new String[]{"\\{", "\\}"}, defaultLang.getClass().getField(field.getName()).get(defaultLang).toString());
                            if (!placeHolder.isEmpty()) {
                                lore.add(LanguageManager.getLanguage().AVAILABLE_PLACEHOLDERS);
                                lore.addAll(StringUtils.prettySplit(
                                        StringUtils.listToString(placeHolder, ", "), true, 120));
                            }
                            x.add(new Items.ActionItem() {
                                @Override
                                public void onClick(InventoryClickEvent event) {}

                                @Override
                                public ItemStack getItem() {
                                    return ItemUtils.createItem(Material.PAPER, "&7" + field.getName(), 1,
                                            lore, null);
                                }
                            });
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                    return x;
                }), "&6Language: &9" + keyAsString + " &7(&8%current_page%&7/&8%max_page%&7)", 18);
                newInv.addBarItem(9, backItem);
                langCategories.put(keyAsString, newInv);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return items;
    });

    private final Items languageAsItem = new Items(() -> {
        final List<Items.ActionItem> items = new ArrayList<>();
        for(String lang : availableLang().split(", ")){
            lang = lang.replace("&7", "");
            String finalLang = lang;
            items.add(new Items.ActionItem() {
                @Override
                public void onClick(InventoryClickEvent event) {}

                @Override
                public ItemStack getItem() {
                    return ItemUtils.infoItem(Material.BOOK, "&7" + finalLang,
                            "&8Left click to &6set &8this language\n&8Right click to &cdelete &8this language",
                            50, finalLang.equals(Config.LANG));
                }
            });
        }
        return items;
    });

    public String availableLang() {
        StringBuilder str = new StringBuilder();
        for(File file : Objects.requireNonNull(Config.LANGUAGE_FOLDER.listFiles())) {
            if(file.getName().contains("language_"))
                str.append(", ").append(file.getName().replace("language_", "&7").replace(".json", ""));
        }
        return str.toString().replaceFirst(", ", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        if(mainLanguageEditor == null) {
            mainLanguageEditor = new MultipleInventory(allLangItems, "&6Language &7(&8%current_page%&7/&8%max_page%&7)", 27);
            mainLanguageEditor.addBarItem(1, editLangItem);
            mainLanguageEditor.display((Player) sender);
        } else {
            mainLanguageEditor.update((Player) sender);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(mainLanguageEditor == null || event.getCurrentItem() == null) return;
        if(!event.getInventory().equals(mainLanguageEditor.getCurrentInventory())) return;
        event.setCancelled(true);
        if(mainLanguageEditor.isSysItem(event.getCurrentItem())) return;
        lang = LanguageManager.getLanguage();
        final ItemStack item = event.getCurrentItem();
        if(item.getType() == Material.BOOK) {
            onCategoriesClicked(event);
        } else if(item.equals(editLangItem.buildFirst())) {
            onEditLanguageClicked(event);
        }
    }

    @EventHandler
    public void onCategoriesLanguageClick(InventoryClickEvent event) {
        if(langCategories.isEmpty() || event.getCurrentItem() == null) return;
        MultipleInventory inventory = null;
        for(MultipleInventory inv : langCategories.values())
            if (event.getInventory().equals(inv.getCurrentInventory()))
                inventory = inv;
        if(inventory == null) return;
        event.setCancelled(true);
        final ItemStack item = event.getCurrentItem();
        if(inventory.isSysItem(item)) return;
        
        Player player = (Player) event.getWhoClicked(); 
        lang = LanguageManager.getLanguage();
        if(item.getType() == Material.PAPER) {
            onLangItemClicked(event, inventory);
        } else if (item.equals(backItem.buildFirst())) {
            mainLanguageEditor.display(player);
        }
    }
    
    private void onCategoriesClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        langCategories.get(ColorUtils.strip(event.getCurrentItem().getItemMeta().getDisplayName())).display(player);
    }

    private void onLangItemClicked(InventoryClickEvent event, MultipleInventory currentInventory) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(lang.NEW_VALUE_TEXT, "{complement}->"+item.getItemMeta().getDisplayName().toLowerCase().replaceAll("_", " "))));
        new StringAsk(player,
            respond -> {
                if (respond.equalsIgnoreCase(lang.CANCEL_KEYWORD)) return;
                try {
                    lang.getClass().getDeclaredField(ColorUtils.strip(item.getItemMeta().getDisplayName())).set(lang, respond);
                    Main.getInstance().getLanguageManager().saveLanguage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                BukkitTasks.sync(()->currentInventory.update(player));
        });
    }
    private void onEditLanguageClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(languageEditor == null) {
            languageEditor = new MultipleInventory(languageAsItem, "&6Language Type &7(&8%current_page%&7/&8%max_page%&7)", 18);
            languageEditor.addBarItem(1, createLanguageItem).addBarItem(9, backItem);
            languageEditor.display(player);
        } else {
            languageEditor.update(player);
        }
    }
    private void onCreateLangItemClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        new StringAsk(player,
            sRespond -> {
                languageEditor.update(player);
                final String askedLang = sRespond.toUpperCase();
                if (askedLang.equalsIgnoreCase(lang.CANCEL_KEYWORD)) return;
                if(FileUtils.isExist(Config.LANGUAGE_FOLDER, "language_" + askedLang + ".json")) {
                    player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(lang.LANGUAGE_ALREADY_EXIST, "{lang}->" + askedLang)));
                } else {
                    Main.getInstance().getLanguageManager().createLanguage(askedLang, true);
                }
        });
    }


    @EventHandler
    public void onEditorInventoryClick(InventoryClickEvent event) {
        if(languageEditor == null || event.getCurrentItem() == null) return;
        if(!event.getInventory().equals(languageEditor.getCurrentInventory())) return;
        event.setCancelled(true);
        if(languageEditor.isSysItem(event.getCurrentItem())) return;
        lang = LanguageManager.getLanguage();

        final ClickType clickType = event.getClick();
        final ItemStack item = event.getCurrentItem();
        final Player player = (Player) event.getWhoClicked();

        if(item.getType() == Material.BOOK) {
            String lang = ColorUtils.strip(item.getItemMeta().getDisplayName());
            LanguageManager languageManager = Main.getInstance().getLanguageManager();

            if(clickType.isLeftClick()) {
                languageManager.modifyLanguage(lang.toUpperCase());
                mainLanguageEditor.update();
                languageEditor.update(player);
            }
            else if (clickType.isRightClick()) {
                if(Config.LANG.equals(lang)) {
                    player.sendMessage(ColorUtils.translate(this.lang.CANNOT_DELETE_CURRENT_LANG));
                }
                else {
                    if(event.isShiftClick()) {
                        FileUtils.deleteFile(Config.LANGUAGE_FOLDER, "language_" + lang + ".json");
                        languageEditor.update(player);
                    }
                    else {
                        new BooleanAsk(player, StringUtils.fastReplace(this.lang.CONFIRMATION_TO_DELETE, "{toDelete}->" + lang + " language"),
                            respond -> {
                                if (respond) {
                                    FileUtils.deleteFile(Config.LANGUAGE_FOLDER, "language_" + lang + ".json");
                                }
                                languageEditor.update(player);
                            });
                    }
                }
            }
        } else if (item.equals(createLanguageItem.buildFirst())) {
            onCreateLangItemClicked(event);
        } else if (item.equals(backItem.buildFirst())) {
            mainLanguageEditor.display(player);
        }
    }
}
