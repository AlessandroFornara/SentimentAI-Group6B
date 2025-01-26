export { badgeImages, capitalizeWords };

const badgeImages = {
    activityBasedBadge: {
        1: {
            path: require('@/assets/ActivityBasedBadges/ActivityStarter.png'),
            name: 'Activity Starter',
            nameIt: 'Iniziatore di Attività'
        },
        2: {
            path: require('@/assets/ActivityBasedBadges/TaskExplorer.png'),
            name: 'Task Explorer',
            nameIt: 'Esploratore di Compiti'
        },
        3: {
            path: require('@/assets/ActivityBasedBadges/ActionAchiever.png'),
            name: 'Action Achiever',
            nameIt: 'Realizzatore di Azioni'
        },
        4: {
            path: require('@/assets/ActivityBasedBadges/MasterOfActivities.png'),
            name: 'Master Of Activities',
            nameIt: 'Maestro delle Attività'
        },
    },
    levelBasedBadge: {
        1: {
            path: require('@/assets/LevelBasedBadges/EmotionalAwakener.png'),
            name: 'Emotional Awakener',
            nameIt: 'Risveglio Emozionale'
        },
        2: {
            path: require('@/assets/LevelBasedBadges/MoodNavigator.png'),
            name: 'Mood Navigator',
            nameIt: 'Navigatore di Emozioni'
        },
        3: {
            path: require('@/assets/LevelBasedBadges/HarmonyBuilder.png'),
            name: 'Harmony Builder',
            nameIt: 'Costruttore di Armonia'
        },
        4: {
            path: require('@/assets/LevelBasedBadges/SentientSage.png'),
            name: 'Sentient Sage',
            nameIt: 'Saggio Sentiente'
        },
    },
    timeBasedBadge: {
        1: {
            path: require('@/assets/TimeBasedBadges/2.png'),
            name: '2 Days',
            nameIt: '2 Giorni'
        },
        2: {
            path: require('@/assets/TimeBasedBadges/5.png'),
            name: '5 Days',
            nameIt: '5 Giorni'
        },
        3: {
            path: require('@/assets/TimeBasedBadges/10.png'),
            name: '10 Days',
            nameIt: '10 Giorni'
        },
        4: {
            path: require('@/assets/TimeBasedBadges/20.png'),
            name: '20 Days',
            nameIt: '20 Giorni'
        },
    },
    topicBasedBadge: {
        1: {
            path: require('@/assets/TopicBasedBadges/BeginnerExplorer.png'),
            name: 'Beginner Explorer',
            nameIt: 'Esploratore Principiante'
        },
        2: {
            path: require('@/assets/TopicBasedBadges/CuriousThinker.png'),
            name: 'Curious Thinker',
            nameIt: 'Pensatore Curioso'
        },
        3: {
            path: require('@/assets/TopicBasedBadges/TopicTrailblazer.png'),
            name: 'Topic Trailblazer',
            nameIt: 'Pioniere di Argomenti'
        },
        4: {
            path: require('@/assets/TopicBasedBadges/MasterOfInsights.png'),
            name: 'Master Of Insights',
            nameIt: 'Maestro di Intuizioni'
        },
    }
};


function capitalizeWords(str) {
    return str
        .replace(/([A-Z])/g, ' $1')
        .trim()
        .toLowerCase()
        .replace(/\b\w/g, (char) => char.toUpperCase());
}