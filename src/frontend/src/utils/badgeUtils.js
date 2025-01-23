export { badgeImages, capitalizeWords };

const badgeImages = {
    activityBasedBadge: {
        1: {path: require('@/assets/ActivityBasedBadges/ActivityStarter.png'), name: 'Activity Starter'},
        2: {path: require('@/assets/ActivityBasedBadges/TaskExplorer.png'), name: 'Task Explorer'},
        3: {path: require('@/assets/ActivityBasedBadges/ActionAchiever.png'), name: 'Action Achiever'},
        4: {path: require('@/assets/ActivityBasedBadges/MasterOfActivities.png'), name: 'Master Of Activities'},
    },
    levelBasedBadge: {
        1: {path: require('@/assets/LevelBasedBadges/EmotionalAwakener.png'), name: 'Emotional Awakener'},
        2: {path: require('@/assets/LevelBasedBadges/MoodNavigator.png'), name: 'Mood Navigator'},
        3: {path: require('@/assets/LevelBasedBadges/HarmonyBuilder.png'), name: 'Harmony Builder'},
        4: {path: require('@/assets/LevelBasedBadges/SentientSage.png'), name: 'Sentient Sage'},
    },
    timeBasedBadge: {
        1: {path: require('@/assets/TimeBasedBadges/2.png'), name: '2 Days'},
        2: {path: require('@/assets/TimeBasedBadges/5.png'), name: '5 Days'},
        3: {path: require('@/assets/TimeBasedBadges/10.png'), name: '10 Days'},
        4: {path: require('@/assets/TimeBasedBadges/20.png'), name: '20 Days'},
    },
    topicBasedBadge: {
        1: {path: require('@/assets/TopicBasedBadges/BeginnerExplorer.png'), name: 'Beginner Explorer'},
        2: {path: require('@/assets/TopicBasedBadges/CuriousThinker.png'), name: 'Curious Thinker'},
        3: {path: require('@/assets/TopicBasedBadges/TopicTrailblazer.png'), name: 'Topic Trailblazer'},
        4: {path: require('@/assets/TopicBasedBadges/MasterOfInsights.png'), name: 'Master Of Insights'},
    }
};

function capitalizeWords(str) {
    return str
        .replace(/([A-Z])/g, ' $1')
        .trim()
        .toLowerCase()
        .replace(/\b\w/g, (char) => char.toUpperCase());
}