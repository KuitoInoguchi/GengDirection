-- ============================================================
-- 梗向 GengDirection — 预制数据（运行前确保 data.sql 已执行）
-- ============================================================
USE geng_direction;

-- --------------------------------------------------------
-- 额外用户（评论者）
-- --------------------------------------------------------
INSERT INTO user_info (username, password_hash, nickname, status) VALUES
('xiaohua',   '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '小花花',   1),
('laozhang',  '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '老张很懂事', 1),
('miaomiao',  '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '喵喵酱',   1),
('geek404',   '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '404找不到', 1),
('sunner',    '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '阳光男孩', 1),
('qiuqiu',   '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '球球别卷了', 1),
('tangping',  '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '躺平大师', 1),
('worker007', '$2a$10$dummyhash00000000000000000000000000000000000000000000000', '007打工人', 1)
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

-- --------------------------------------------------------
-- 标签
-- --------------------------------------------------------
INSERT INTO tag_info (tag_name) VALUES
('热点'), ('职场'), ('情绪'), ('网络用语'), ('生活'),
('表情包'), ('校园'), ('游戏'), ('社交'), ('时尚')
ON DUPLICATE KEY UPDATE tag_name = VALUES(tag_name);

-- --------------------------------------------------------
-- 30 条梗帖（含详细释义）
-- --------------------------------------------------------
INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '内卷',
'内卷（nèijuǎn），全称"内卷化"（involution），原是社会学术语，指一种社会文化模式在某发展阶段后停止进化并向内发展。如今泛指同一阶层内部为争夺有限资源而进行的低水平激烈竞争，越卷越累，越卷越内耗。

典型场景：同学甲把 PPT 做成动画，同学乙就做成 3D；老板发现大家都加班到 9 点，于是把 9 点定为正常下班时间。

一句话总结：不是我想卷，是大家都在卷，我不卷就输了。',
'https://baike.baidu.com/item/%E5%86%85%E5%8D%B7/56434743',
(SELECT id FROM user_info WHERE username = 'admin'),
4800, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '躺平',
'躺平，是与"内卷"相对的一种生活态度，指放弃追求、降低欲望、不参与高压竞争，用最低消耗维持生活。

源起：2021 年某网帖"躺平即是正义"引发巨大共鸣，随后迅速出圈。躺平者认为：不买房、不结婚、低消费、维持温饱，拒绝成为机器。

但注意：真正的躺平不是颓废，而是一种对过度竞争的消极抵制，是对人生节奏的主动选择。',
NULL,
(SELECT id FROM user_info WHERE username = 'admin'),
4600, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT 'yyds（永远的神）',
'YYDS 是"永远的神"的汉语拼音首字母缩写，用于表达对某人或某事物极致的崇拜与赞美，等同于"绝了""最牛""封神"。

起源：最早来自电竞圈，粉丝为爱豆或职业选手加油助威时使用。后经 B 站、微博广泛传播，成为全民流行词。

用法举例：
· 这碗螺蛳粉，yyds！
· 科比永远的神，yyds！

注意：用烂了会显得很土，慎用。',
NULL,
(SELECT id FROM user_info WHERE username = 'student01'),
4200, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '耗子尾汁',
'耗子尾汁，是"好自为之"的谐音梗，意思是要好好约束、反省自己的行为，别乱来。

来源：出自香港科幻作家倪匡（笔名卫斯理）晚年的一段访谈视频，他用浓重粤语口音说出"好自为之"，发音酷似"耗子尾汁"，被网友截取后疯狂传播。

日常用法：当朋友做了傻事，你就可以用一个严肃的表情回复"耗子尾汁"，效果极佳。',
NULL,
(SELECT id FROM user_info WHERE username = 'student01'),
3900, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '打工人',
'打工人，自嘲又励志的身份标签，泛指每天上班、靠出卖劳动力换取薪水的普通劳动者。

来源：2020 年底，一段"打工人，打工魂，打工都是人上人"的配音视频火遍全网，将这一身份带入大众视野。

特点：打工人永远在路上，早起奋斗，却始终改变不了现状。既是自嘲，也是一种苦中作乐的精神胜利法。

名言：早安，打工人！（每天早上的自我鼓励）',
NULL,
(SELECT id FROM user_info WHERE username = 'admin'),
4100, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '摆烂',
'摆烂，指明知道事情可能会变得更糟，但仍放弃努力，任其发展，甚至故意加速其败坏的行为或态度。

来源：源自足球圈，指球队为了获得更好的选秀顺位而故意输球。后引申为一种消极应对的人生态度。

摆烂 vs 躺平：躺平是"不参与"，摆烂是"参与了但不好好来"，甚至破罐子破摔。

经典句式："算了，摆烂了。"（面对死线、期末考试、人生困境时的最终选择）',
NULL,
(SELECT id FROM user_info WHERE username = 'student01'),
3700, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '绝绝子',
'"绝了"的夸张升级版，既可以表示极度赞赏（绝了），也可以表示极度无语（绝了……），完全看语境和语气。

来源：由网络综艺粉丝圈发展而来，后被主流网络语言采纳，风靡一时。

用法示例：
· （正面）这个蛋糕也太好吃了吧，绝绝子！
· （负面）他居然迟到两小时还说堵车，绝绝子……

该词在 2021 年入选多家媒体年度热词，现已有轻微"过气"迹象，但仍广泛使用。',
NULL,
(SELECT id FROM user_info WHERE username = 'xiaohua'),
3200, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '破防了',
'破防，指心理防线被攻破。"破防了"用来描述被某事深深触动，情绪失控，或感动落泪，或愤怒到无法平静。

来源：原为游戏术语，指角色的防御被打穿。后引申为情感领域，表达内心受到冲击。

使用场景：
· 看到这段视频哭成狗，彻底破防了。
· 他这句话说得太准了，我破防了。
· 被老板当众批评，破防了，差点哭出来。

与"感动"不同，破防含有一种措手不及、无力抵抗的被动感。',
NULL,
(SELECT id FROM user_info WHERE username = 'miaomiao'),
3400, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '尊嘟假嘟',
'"真的假的"的谐音梗，用来表达强烈的惊讶、质疑或不敢置信，语气轻松调皮。

发音来自福建/台湾口音对"真的假的"的特殊读法，因发音特别可爱，被网友广泛采用。

用法：
· A：我昨天中了 500 万！
· B：尊嘟假嘟？！

· 这道题这么简单你都做错了？尊嘟假嘟？

常与表情包搭配使用，配上一张瞪大眼睛的猫咪图，效果翻倍。',
NULL,
(SELECT id FROM user_info WHERE username = 'geek404'),
2800, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '泼天的富贵',
'"泼天的富贵"形容突然降临的、巨大的流量、机遇或财富，有时候来得让人猝不及防，主体可能无福消受或措手不及。

出处：2023-2024 年间，某些品牌或明星因为意外事件突然获得海量流量，网友们便用"这泼天的富贵，能不能接住？"来描述这种局面。

延伸用法：
· 运气好到爆的人：哇，泼天的富贵都让你赶上了。
· 自嘲流量太大：这泼天的富贵，我根本接不住……',
NULL,
(SELECT id FROM user_info WHERE username = 'laozhang'),
2600, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '班味',
'班味，指长期上班后身上沾染的一种特殊气质——疲惫、麻木、说话官方、满身压力，彻底告别了学生时代的活泼与自由。

描述：刚毕业的年轻人经过几个月的职场洗礼，整个人气质大变，眼神涣散，头发稀疏，思维僵化，说话开始"汇报工作"，这就是"班味上身了"。

相关表达：
· "你身上班味好重。"（你已经被职场同化了）
· 假期结束：班味又要来了……
· 刚毕业时：我绝不会变成那样的人（一年后完全变成那样）',
NULL,
(SELECT id FROM user_info WHERE username = 'worker007'),
2900, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '松弛感',
'松弛感，指一种从容不迫、不被外界裹挟的淡定气质，无论发生什么都能保持内心平和，优雅应对，不焦虑、不急躁。

近年来因某明星家庭在机场的淡定表现走红——行李丢了也不慌乱，堪称"松弛感教科书"，引发全网热议。

怎么有松弛感？
· 遇事不慌，先深呼吸；
· 不把每件事都当天大的事；
· 允许事情不完美，接纳不确定性；
· 有安全感，不依赖外界认可。

松弛感 ≠ 不努力，而是努力但不焦虑。',
NULL,
(SELECT id FROM user_info WHERE username = 'xiaohua'),
3100, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '多巴胺穿搭',
'多巴胺穿搭，指用大量鲜艳明亮的撞色服装搭配，通过视觉刺激让人获得愉悦感，就像摄入多巴胺一样让心情快乐。

背景：源自色彩心理学，亮丽颜色能影响情绪。2023 年夏季，全网掀起多巴胺穿搭浪潮，街上满是穿着荧光黄、糖果粉、电光蓝的年轻人。

代表色：明黄、橙红、薄荷绿、粉紫、天蓝
搭配原则：大胆撞色，宁可多彩也不要单调，色系和谐不冲突即可。

反义词：核污水穿搭（灰蒙蒙、没精气神）',
NULL,
(SELECT id FROM user_info WHERE username = 'miaomiao'),
2500, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT 'City不City',
'"City不City"用来询问某事物是否足够时髦、洋气、有都市感，带有一种轻松诙谐的语气。

来源：2024年，一位名叫Paul的外国博主在中国旅行，频繁用"City不City？"来描述他见到的新奇事物，因其口音搞笑又可爱，迅速爆红全网。

用法：
· "今天这个造型City不City？"
· "这家餐厅太City了！"
· "这个操作有点不City啊。"

该梗的核心在于"City"的谐音感和外国人视角带来的新鲜感。',
NULL,
(SELECT id FROM user_info WHERE username = 'geek404'),
3300, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '摸鱼',
'摸鱼，指在工作或学习时间偷偷做与正事无关的事情，例如刷手机、聊天、打游戏，表面上看起来在工作，实则在"划水"。

来源：出自成语"浑水摸鱼"，后简化为"摸鱼"，专指上班划水偷懒。

摸鱼八大境界：
1. 刷朋友圈  2. 逛淘宝  3. 看短视频  4. 追番
5. 玩手游  6. 写私活  7. 投简历  8. 在厕所睡觉

摸鱼哲学：摸鱼不是懒，是在高压工作中给自己续命的生存策略。',
NULL,
(SELECT id FROM user_info WHERE username = 'worker007'),
3600, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '画大饼',
'画大饼，指许下诱人但无法或无意兑现的承诺，用美好前景忽悠、稳住对方的行为。

来源：画出来的饼只能看不能吃，用来比喻空头支票。

典型场景：
· 老板对员工说："好好干，年底给你涨薪！"（次年：今年经济不好，大家共度难关）
· 创业公司：加入我们，未来你有期权，等上市你就财富自由了！
· 领导开会：这个项目是公司的战略重心，做好了前途无量！

识别方法：饼画得越香、越大，越要小心。',
NULL,
(SELECT id FROM user_info WHERE username = 'laozhang'),
3000, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT 'PUA',
'PUA（Pick-up Artist）原指搭讪艺术，后演变为指通过持续的语言操控、精神打压来降低对方自尊心，使其产生自我怀疑、依附和服从的一种心理控制行为。

职场PUA表现：
· "你已经很幸运了，换了别人早开除了。"
· "这点小事都做不好，你是怎么毕业的？"
· "别公司要你？你再想想。"

亲密关系PUA：不断否定对方价值，制造情感落差，让对方觉得"没有我你什么都不是"。

识别和远离PUA：建立自我价值认同，遇到持续打压你的人要保持警觉。',
NULL,
(SELECT id FROM user_info WHERE username = 'admin'),
3800, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '工具人',
'工具人，指在某段关系中只有使用价值、不被真心对待、随时可被替换的人。被当成工具用完即丢。

典型工具人画像：
· 随叫随到，永远备用，但从不被当作重要的人；
· 帮忙搬东西、修电脑、写材料，事后不见踪影；
· 对方在感情困惑时找你倾诉，一旦有了新欢，你就消失在视野里。

来源：网络流行语，最初用于描述恋爱关系中被当备胎的一方，后扩展至友情、职场等各种关系。

如何避免成为工具人：学会说"不"，建立边界感。',
NULL,
(SELECT id FROM user_info WHERE username = 'qiuqiu'),
2700, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '社恐 vs 社牛',
'社恐（社交恐惧症）：在社交场合感到极度不适、焦虑、手足无措，害怕与陌生人交谈，宁愿独处。

社牛（社交牛逼症）：与社恐相反，可以毫无心理障碍地融入任何场合，跟任何人都能自来熟，甚至在公共场合做出让旁观者窘迫的事，本人毫不在意。

有趣组合：
· 社恐 + 社牛 搭档出行 = 社恐躲在社牛身后，由社牛负责一切社交；
· 社恐本人在外人面前极度安静，在熟人面前话痨到让人崩溃。

大多数人其实是"假社恐"——只是懒得社交，并非真正恐惧。',
NULL,
(SELECT id FROM user_info WHERE username = 'miaomiao'),
2300, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '小丑竟是我自己',
'"小丑竟是我自己"，用于自嘲在某件事上表现得荒唐可笑，或被人耍弄后发现自己才是那个傻子的情境。

来源：出自经典台词"小丑竟是我自己"，指一场闹剧中，自以为置身局外的人，最终发现自己才是最大的笑料。

使用场景：
· 精心准备的惊喜被提前发现了：小丑竟是我自己。
· 以为男神对我有意思，结果人家只是普通客气：小丑竟是我自己。
· 熬夜写完的报告发现发错文件夹了：小丑竟是我自己。

该梗带有一种苦涩的自我解嘲，越惨用得越对。',
NULL,
(SELECT id FROM user_info WHERE username = 'sunner'),
2900, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '草台班子',
'草台班子，原指简陋临时搭建的戏台上演出的劣质戏班，后引申为表面光鲜、内里混乱、人员素质参差不齐的团队或组织。

近年出圈：有人分享了"世界是个草台班子"的观察——许多看起来专业、权威的机构内部其实乱成一锅粥，靠的是"差不多先生"撑着。

感悟：当你在职场中发现流程混乱、决策随意、全靠关系运转时，不必过度焦虑，因为大多数地方都是草台班子，只要你比草台班子强一点点，就能混得不错。',
NULL,
(SELECT id FROM user_info WHERE username = 'laozhang'),
3500, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT 'EMO',
'EMO，源自地下音乐文化（emotional hardcore），如今在网络上专指情绪低落、沮丧、丧气、心情极差的状态，是年轻人的情绪口头禅。

使用场景：
· "最近好EMO，啥都不想干。"
· "看完这部剧直接EMO了三天。"
· "被分手了，EMO到极点。"

EMO触发器：
失恋、挂科、被骂、钱不够花、看到别人过得很好、深夜一个人发呆……

应对EMO：听歌、撸猫、吃东西、睡觉，或者干脆让自己EMO够了再说。',
NULL,
(SELECT id FROM user_info WHERE username = 'qiuqiu'),
2400, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '整顿职场',
'"整顿职场"，指初入职场的年轻人对不合理的潜规则、剥削行为和虚伪文化发起挑战，拒绝接受"这就是职场的规矩"这种说法。

整顿行为举例：
· 准时下班，不接受无偿加班；
· 直接拒绝"感情投资"式的人情请托；
· 在群里直接怼画大饼的领导；
· 试用期被压榨直接走人，一点不惯着。

评价两极化：
· 支持派：凭什么忍？打工人要有尊严！
· 保守派：社会就是这样，太嫩了。

真相：整顿职场者需要有过硬的能力或足够的退路作为底气。',
NULL,
(SELECT id FROM user_info WHERE username = 'worker007'),
3200, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT 'DNA动了',
'"DNA动了"，指某事触发了内心深处的本能反应，让人控制不住地产生强烈的共鸣、兴奋或冲动，仿佛刻在基因里的记忆被唤醒。

来源：来自生物学概念，DNA是遗传信息的载体，"DNA动了"暗示这种反应是天生的、本能的，无法抗拒。

典型场景：
· 看到美食视频：我的DNA动了，必须去吃！
· 下雨天想睡觉：下雨天睡觉，DNA动了。
· 听到熟悉的旋律：这首歌一响，DNA直接动了。

该梗形象地表达了那种"不由自主"的本能冲动。',
NULL,
(SELECT id FROM user_info WHERE username = 'sunner'),
2600, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '老六',
'老六，网络黑话，源自狼人杀等推理游戏，形容一个人极其阴险狡猾、喜欢暗中布局、在背后搞小动作，表面老实但实则很会算计。

由来：在狼人杀中，6号位往往被认为是"刀人"的高概率位置，久而久之"老六"就成了阴险之人的代称。

用法：
· "这个人说话滴水不漏，老六本六了。"
· "我就知道是你！果然是老六！"
· "老六行为"：表面配合，背地里暗算；明明有牌，装作没有。

现多用于游戏、日常调侃，不含严重贬义，更多是一种戏谑夸赞。',
NULL,
(SELECT id FROM user_info WHERE username = 'geek404'),
3700, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '显眼包',
'显眼包，指特别爱出风头、总是第一个跳出来引人注意的人，无论是语言还是行为，都比旁人更抢眼、更夸张，生怕别人不注意到自己。

语气：带有调侃意味，但通常不是严重批评，更多是一种戏谑昵称。

典型显眼包行为：
· 全班最安静时突然大声说废话；
· 合影时做最夸张的鬼脸；
· 发言时必须比别人声音大一倍；
· 走到哪儿都是话题中心。

进化版用法："显眼包本包"，用于自嘲自己太爱出风头。',
NULL,
(SELECT id FROM user_info WHERE username = 'xiaohua'),
2100, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '精神状态',
'"精神状态"常被用来质疑或调侃某人的行为举止异于常人，意思是"你没事吧？"或"你最近状态不对劲。"

典型用法：
· "你今天精神状态有点不对啊？"（你今天怎么这么怪？）
· "这哥们精神状态……"（这个人已经不正常了）
· "还好吧你，精神状态令我担忧。"

反向用法（积极）：
· "精神状态绝佳！"——形容状态极好，能量满满。
· "今天精神状态在线。"

总结：精神状态是个中性词，但在网络语境里多用于指出别人（或自己）的怪异之处，带有一丝关心式的吐槽。',
NULL,
(SELECT id FROM user_info WHERE username = 'tangping'),
1800, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '全村的希望',
'"全村的希望"，指被整个家庭甚至村子寄予厚望的孩子，通常是家里读书最好的那一个，背负着所有人对改变命运的期待，压力极大。

自嘲使用：现多用于自嘲承担压力过多，或在某件事上被过高期待的情况。

场景：
· 考研时：我是全村的希望，不能失败。
· 找工作时：爸妈和亲戚都等着我衣锦还乡，全村的希望啊。
· 玩游戏时：队友都指望我，全村的希望呢。

该梗折射出中国家庭对教育改变命运的深层期待，也道出了年轻一代所承受的无形压力。',
NULL,
(SELECT id FROM user_info WHERE username = 'tangping'),
2000, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '卷王',
'卷王，指在内卷环境中卷得最凶、最努力（或最会让别人难受）的那个人，他/她的存在加速了整个群体的内卷化进程。

卷王特征：
· 课后继续刷题到凌晨 2 点；
· PPT 做了 80 页，比要求多 70 页；
· 主动问老师有没有"附加题"；
· 假期也在学，还让你知道他在学。

两种情感：
· 卷王本人：可能是焦虑驱动，也可能是真的热爱；
· 身边的人：又爱又恨，爱他让群体进步，恨他让自己显得不努力。

本质反思：卷王不是敌人，是内卷文化的产物和受害者。',
NULL,
(SELECT id FROM user_info WHERE username = 'student01'),
3100, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '不是，哥们',
'"不是，哥们"是一种万能的开头语，用于表达对某件事的极度震惊、困惑或无语，语气可以从温和的"你这样真的好吗"到强烈的"你这到底在干什么"。

来源：最初为北方日常口语，后经网络传播成为全国性梗语。

使用模板：
· 不是，哥们，你这样真的合适吗？
· 不是，哥们，这道题这么简单你也做错了？
· 不是，哥们，你是什么神仙操作？

进阶变体：
· "不是，姐妹……"
· "不是，这哥们……"（第三人称吃瓜版）

该梗的精髓在于开头那个"不是"——表达了说话人内心极度混乱的第一反应。',
NULL,
(SELECT id FROM user_info WHERE username = 'geek404'),
4000, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

INSERT INTO geng_post (title, content, source_url, author_id, heat_score, status)
SELECT '我不李姐',
'"我不李姐"是"我不理解"的谐音梗，在表达完全不懂、一脸懵的同时，也带有一种"你说什么我都不懂、也不想懂"的无辜感。

来源：谐音字替换是网络语言的常见手法，"理解"→"李姐"，配上一张懵懵的女生头像，效果满分。

使用场景：
· 看天书一样的数学题：我不李姐。
· 被领导说了一堆术语：我不李姐，也不王姐。
· 朋友讲了个你完全听不懂的笑话：啊？我不李姐。

变体："我不李姐，也不王姐，更不张姐。"（表示完全一头雾水的升级版）',
NULL,
(SELECT id FROM user_info WHERE username = 'xiaohua'),
1900, 2
ON DUPLICATE KEY UPDATE heat_score = VALUES(heat_score);

-- --------------------------------------------------------
-- 标签关联
-- --------------------------------------------------------
INSERT INTO geng_post_tag (post_id, tag_id)
SELECT p.id, t.id FROM geng_post p JOIN tag_info t
ON (p.title = '内卷'          AND t.tag_name IN ('热点','职场'))
OR (p.title = '躺平'          AND t.tag_name IN ('热点','生活'))
OR (p.title = 'yyds（永远的神）' AND t.tag_name IN ('热点','网络用语'))
OR (p.title = '耗子尾汁'      AND t.tag_name IN ('热点','网络用语'))
OR (p.title = '打工人'         AND t.tag_name IN ('热点','职场'))
OR (p.title = '摆烂'           AND t.tag_name IN ('生活','情绪'))
OR (p.title = '绝绝子'         AND t.tag_name IN ('网络用语','表情包'))
OR (p.title = '破防了'         AND t.tag_name IN ('情绪','网络用语'))
OR (p.title = '尊嘟假嘟'       AND t.tag_name IN ('网络用语','表情包'))
OR (p.title = '泼天的富贵'     AND t.tag_name IN ('热点','网络用语'))
OR (p.title = '班味'           AND t.tag_name IN ('职场','生活'))
OR (p.title = '松弛感'         AND t.tag_name IN ('生活','热点'))
OR (p.title = '多巴胺穿搭'     AND t.tag_name IN ('时尚','热点'))
OR (p.title = 'City不City'     AND t.tag_name IN ('热点','网络用语'))
OR (p.title = '摸鱼'           AND t.tag_name IN ('职场','生活'))
OR (p.title = '画大饼'         AND t.tag_name IN ('职场','社交'))
OR (p.title = 'PUA'            AND t.tag_name IN ('社交','情绪'))
OR (p.title = '工具人'         AND t.tag_name IN ('社交','情绪'))
OR (p.title = '社恐 vs 社牛'   AND t.tag_name IN ('社交','网络用语'))
OR (p.title = '小丑竟是我自己' AND t.tag_name IN ('表情包','情绪'))
OR (p.title = '草台班子'       AND t.tag_name IN ('职场','热点'))
OR (p.title = 'EMO'            AND t.tag_name IN ('情绪','网络用语'))
OR (p.title = '整顿职场'       AND t.tag_name IN ('职场','热点'))
OR (p.title = 'DNA动了'        AND t.tag_name IN ('网络用语','表情包'))
OR (p.title = '老六'           AND t.tag_name IN ('游戏','网络用语'))
OR (p.title = '显眼包'         AND t.tag_name IN ('网络用语','社交'))
OR (p.title = '精神状态'       AND t.tag_name IN ('网络用语','情绪'))
OR (p.title = '全村的希望'     AND t.tag_name IN ('校园','生活'))
OR (p.title = '卷王'           AND t.tag_name IN ('校园','职场'))
OR (p.title = '不是，哥们'     AND t.tag_name IN ('热点','网络用语'))
OR (p.title = '我不李姐'       AND t.tag_name IN ('网络用语','表情包'))
ON DUPLICATE KEY UPDATE post_id = VALUES(post_id);

-- --------------------------------------------------------
-- 预制评论
-- --------------------------------------------------------
INSERT INTO post_comment (post_id, user_id, content)
SELECT p.id, u.id, c.content FROM geng_post p
JOIN (VALUES
  ROW('内卷', 'laozhang',  '我们组就有个卷王，每天凌晨一点还在钉钉发消息，给整个组制造焦虑'),
  ROW('内卷', 'miaomiao',  '内卷的本质是存量博弈，蛋糕不大了，大家只好互相抢'),
  ROW('内卷', 'geek404',   '解决内卷的方法只有一个：要么做大蛋糕，要么离开这张桌子'),
  ROW('内卷', 'sunner',    '我室友昨晚学到4点，我直接睡了，早上醒来感觉自己输了'),
  ROW('躺平', 'worker007', '躺平不是不努力，是拒绝为不合理的系统再努力'),
  ROW('躺平', 'qiuqiu',    '我躺了两年，发现还是得爬起来，但爬起来的心态完全不同了'),
  ROW('躺平', 'xiaohua',   '躺平是权利，但前提是你得能负担得起躺平的成本'),
  ROW('yyds（永远的神）', 'laozhang', 'YYDS这词现在连我爸都会用了，彻底过气了属于是'),
  ROW('yyds（永远的神）', 'miaomiao', '但还是好用，找不到更简洁的替代词'),
  ROW('yyds（永远的神）', 'geek404',  '电竞圈出来的词，现在变成了万能赞美模板'),
  ROW('耗子尾汁', 'geek404',   '倪匡老先生万万没想到自己会这么出圈哈哈哈'),
  ROW('耗子尾汁', 'sunner',    '每次朋友犯蠢我就发这三个字，比骂人更有力'),
  ROW('打工人', 'tangping',    '早安，打工人！（发自凌晨六点的地铁上）'),
  ROW('打工人', 'worker007',   '打工人最悲哀的一天：周日晚上十点，已经开始焦虑明天的工作了'),
  ROW('打工人', 'sunner',      '打工人语录：只要我足够努力，老板就能过上更好的生活'),
  ROW('摆烂', 'qiuqiu',        '这学期我已经全面进入摆烂模式了，但奇怪的是心情反而好了'),
  ROW('摆烂', 'xiaohua',       '摆烂≠放弃，是放弃了那些本来就不值得努力的部分'),
  ROW('摆烂', 'laozhang',      '有时候摆烂是一种自我保护，防止过度耗竭'),
  ROW('绝绝子', 'miaomiao',    '这个词真的被用烂了，现在说出来都尴尬'),
  ROW('绝绝子', 'sunner',      '但承认吧，它确实很精准，没有更好的替代品'),
  ROW('破防了', 'worker007',   '看《人世间》时从头哭到尾，彻底破防'),
  ROW('破防了', 'geek404',     '被领导当众夸了，我也破防了，没想到会这么感动'),
  ROW('尊嘟假嘟', 'qiuqiu',    '这个词发音出来就会笑，天生的解压神器'),
  ROW('尊嘟假嘟', 'miaomiao',  '每次听到这个词都忍不住模仿发音，太洗脑了'),
  ROW('泼天的富贵', 'xiaohua', '某咖啡品牌因为代言人暴雷反而销量暴涨，泼天的富贵接住了'),
  ROW('泼天的富贵', 'laozhang','真正的考验不是富贵来不来，而是来了能不能接住'),
  ROW('班味', 'laozhang',      '寒假结束后第一天，我妈说我身上有一股陌生的气息，那就是班味'),
  ROW('班味', 'tangping',      '班味的反义词是学生气，失去的东西永远回不来了'),
  ROW('班味', 'worker007',     '我已经无法和大学时的自己产生共情了，班味太重'),
  ROW('松弛感', 'miaomiao',    '松弛感是一种稀缺资源，越焦虑的人越难拥有'),
  ROW('松弛感', 'geek404',     '真正的松弛感是内心有底气，不是假装不在乎'),
  ROW('松弛感', 'sunner',      '看完那段机场视频后，我决定也要练出松弛感——结果更焦虑了'),
  ROW('多巴胺穿搭', 'miaomiao', '周末穿了一身荧光绿出门，心情确实变好了，回头率100%'),
  ROW('多巴胺穿搭', 'qiuqiu',  '尝试多巴胺穿搭的第一天，同事问我是不是刚搬完家'),
  ROW('City不City', 'sunner',  'Paul这个外国人真的把"City"这个词用活了'),
  ROW('City不City', 'qiuqiu',  '下次去北京四合院问一句：这City不City？'),
  ROW('摸鱼', 'worker007',     '我把摸鱼时间管理得比正经工作还精细'),
  ROW('摸鱼', 'xiaohua',       '领导眼神一扫过来，0.01秒内切回工作界面，肌肉记忆'),
  ROW('摸鱼', 'laozhang',      '摸鱼中的最高境界：让别人以为你在认真工作，实际上你在规划下一份工作'),
  ROW('画大饼', 'tangping',    '上一份工作画了我一年的饼，最后饼换成了"感谢信"'),
  ROW('画大饼', 'miaomiao',    '如何辨别大饼：凡是涉及"未来""等一等""做好了"之类词语的承诺，都是大饼'),
  ROW('画大饼', 'geek404',     '老板画的饼不敢吃，怕吃完了还得加班消化'),
  ROW('PUA', 'geek404',        '职场PUA最隐蔽的地方在于，受害者很容易误以为是自己的问题'),
  ROW('PUA', 'sunner',         '建立自我价值认同是抵抗PUA的最好武器'),
  ROW('工具人', 'qiuqiu',      '被当工具人多年，某天突然清醒了，立刻建立边界感'),
  ROW('工具人', 'miaomiao',    '学会说"不"之后，我的生活质量提升了200%'),
  ROW('社恐 vs 社牛', 'sunner',    '我确诊了"熟人面前社牛、陌生人面前社恐"综合征'),
  ROW('社恐 vs 社牛', 'geek404',   '社恐和社牛的区别：一个在电梯里看地面，一个和电梯里所有人聊天'),
  ROW('社恐 vs 社牛', 'qiuqiu',    '假社恐在此——不想去的聚会通通自称社恐，想去的聚会立刻变社牛'),
  ROW('小丑竟是我自己', 'worker007', '考前觉得某题一定不考，结果那题占了30分，小丑竟是我自己'),
  ROW('小丑竟是我自己', 'sunner',    '表白被拒后发现自己只是备胎，小丑竟是我自己，笑着笑着就哭了'),
  ROW('草台班子', 'laozhang',  '"世界是个草台班子"这句话让我彻底释然了，不必对自己太苛求'),
  ROW('草台班子', 'tangping',  '见识过大公司的草台班子，才明白能力和平台其实差不多重要'),
  ROW('EMO', 'miaomiao',       'EMO不代表脆弱，只是人类情绪的正常波动'),
  ROW('EMO', 'tangping',       '深夜EMO时间到，打开朋友圈全是伤感文学'),
  ROW('EMO', 'sunner',         '被劈腿后EMO了整整一周，第七天突然悟了：他不配让我EMO'),
  ROW('整顿职场', 'geek404',   '整顿职场的前提是你有整顿的资本，比如一技之长或者财务自由'),
  ROW('整顿职场', 'worker007', '整顿职场最爽的一个词：准时下班。比啥都有效。'),
  ROW('DNA动了', 'geek404',    '闻到教室里的粉笔灰味道，DNA直接动了，瞬间回到小学'),
  ROW('DNA动了', 'sunner',     '听到老歌DNA就动，这是刻在基因里的旋律记忆'),
  ROW('DNA动了', 'xiaohua',    '下雨天躺在床上不想起——DNA动了，这是刻在人类基因里的本能'),
  ROW('老六', 'worker007',     '我们组就有个老六，每次开会一言不发，会后全是他的操作'),
  ROW('老六', 'xiaohua',       '老六最可怕的是他根本不让你发现他是老六'),
  ROW('显眼包', 'xiaohua',     '我就是我们班的显眼包，老师都记住我了——也不知道是好事还是坏事'),
  ROW('显眼包', 'qiuqiu',      '朋友圈的显眼包，每次发自拍都能在评论区引发一场闹剧'),
  ROW('精神状态', 'tangping',  '看完这个帖子我的精神状态：虽然很正常但看起来很不正常'),
  ROW('精神状态', 'geek404',   '精神状态良好（指已经学会用梗评价别人的精神状态了）'),
  ROW('全村的希望', 'tangping', '背负"全村的希望"的人，内心压力往往不被理解，因为大家只看结果'),
  ROW('全村的希望', 'student01','我爸跟亲戚说我学计算机的，"以后肯定就是程序员大牛了"，全村的希望又加了10斤'),
  ROW('卷王', 'student01',     '卷王是生态系统的一部分，消灭卷王不能解决内卷，只会产生新卷王'),
  ROW('卷王', 'qiuqiu',        '室友凌晨还在学习，我正在刷手机——他是卷王，我是摆烂王'),
  ROW('不是，哥们', 'sunner',  '这三个字的信息量堪比一篇论文，开头就说明态度了'),
  ROW('不是，哥们', 'qiuqiu',  '"不是哥们"是当代最万能的对话开场白'),
  ROW('不是，哥们', 'laozhang','不是，哥们，你这数据分析做出来自己看得懂吗？'),
  ROW('我不李姐', 'xiaohua',   '高数课上了一半，我全程脸懵：我不李姐，也不王姐，更不张姐'),
  ROW('我不李姐', 'miaomiao',  '老师说了十分钟，我只记住了"这个很重要"，其余完全不李姐')
) AS c(post_title, commenter, content)
JOIN user_info u ON u.username = c.commenter
WHERE p.title = c.post_title;

-- --------------------------------------------------------
-- 完成
-- --------------------------------------------------------
SELECT '预制数据导入完成！' AS result;
SELECT COUNT(*) AS post_count FROM geng_post;
SELECT COUNT(*) AS comment_count FROM post_comment;
