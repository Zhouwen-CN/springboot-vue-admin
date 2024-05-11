<script lang="ts" setup>
import {defineModel, ref} from 'vue';

const iconList = ['Plus', 'Minus', 'CirclePlus', 'Search', 'Female', 'Male', 'Aim', 'House', 'FullScreen', 'Loading', 'Link', 'Service', 'Pointer', 'Star', 'Notification', 'Connection', 'ChatDotRound', 'Setting', 'Clock', 'Position', 'Discount', 'Odometer', 'ChatSquare', 'ChatRound', 'ChatLineRound', 'ChatLineSquare', 'ChatDotSquare', 'View', 'Hide', 'Unlock', 'Lock', 'RefreshRight', 'RefreshLeft', 'Refresh', 'Bell', 'MuteNotification', 'User', 'Check', 'CircleCheck', 'Warning', 'CircleClose', 'Close', 'PieChart', 'More', 'Compass', 'Filter', 'Switch', 'Select', 'SemiSelect', 'CloseBold', 'EditPen', 'Edit', 'Message', 'MessageBox', 'TurnOff', 'Finished', 'Delete', 'Crop', 'SwitchButton', 'Operation', 'Open', 'Remove', 'ZoomOut', 'ZoomIn', 'InfoFilled', 'CircleCheckFilled', 'SuccessFilled', 'WarningFilled', 'CircleCloseFilled', 'QuestionFilled', 'WarnTriangleFilled', 'UserFilled', 'MoreFilled', 'Tools', 'HomeFilled', 'Menu', 'UploadFilled', 'Avatar', 'HelpFilled', 'Share', 'StarFilled', 'Comment', 'Histogram', 'Grid', 'Promotion', 'DeleteFilled', 'RemoveFilled', 'CirclePlusFilled', 'ArrowLeft', 'ArrowUp', 'ArrowRight', 'ArrowDown', 'ArrowLeftBold', 'ArrowUpBold', 'ArrowRightBold', 'ArrowDownBold', 'DArrowRight', 'DArrowLeft', 'Download', 'Upload', 'Top', 'Bottom', 'Back', 'Right', 'TopRight', 'TopLeft', 'BottomRight', 'BottomLeft', 'Sort', 'SortUp', 'SortDown', 'Rank', 'CaretLeft', 'CaretTop', 'CaretRight', 'CaretBottom', 'DCaret', 'Expand', 'Fold', 'DocumentAdd', 'Document', 'Notebook', 'Tickets', 'Memo', 'Collection', 'Postcard', 'ScaleToOriginal', 'SetUp', 'DocumentDelete', 'DocumentChecked', 'DataBoard', 'DataAnalysis', 'CopyDocument', 'FolderChecked', 'Files', 'Folder', 'FolderDelete', 'FolderRemove', 'FolderOpened', 'DocumentCopy', 'DocumentRemove', 'FolderAdd', 'FirstAidKit', 'Reading', 'DataLine', 'Management', 'Checked', 'Ticket', 'Failed', 'TrendCharts', 'List', 'Microphone', 'Mute', 'Mic', 'VideoPause', 'VideoCamera', 'VideoPlay', 'Headset', 'Monitor', 'Film', 'Camera', 'Picture', 'PictureRounded', 'Iphone', 'Cellphone', 'VideoCameraFilled', 'PictureFilled', 'Platform', 'CameraFilled', 'BellFilled', 'Location', 'LocationInformation', 'DeleteLocation', 'Coordinate', 'Bicycle', 'OfficeBuilding', 'School', 'Guide', 'AddLocation', 'MapLocation', 'Place', 'LocationFilled', 'Van', 'Watermelon', 'Pear', 'NoSmoking', 'Smoking', 'Mug', 'GobletSquareFull', 'GobletFull', 'KnifeFork', 'Sugar', 'Bowl', 'MilkTea', 'Lollipop', 'Coffee', 'Chicken', 'Dish', 'IceTea', 'ColdDrink', 'CoffeeCup', 'DishDot', 'IceDrink', 'IceCream', 'Dessert', 'IceCreamSquare', 'ForkSpoon', 'IceCreamRound', 'Food', 'HotWater', 'Grape', 'Fries', 'Apple', 'Burger', 'Goblet', 'GobletSquare', 'Orange', 'Cherry', 'Printer', 'Calendar', 'CreditCard', 'Box', 'Money', 'Refrigerator', 'Cpu', 'Football', 'Brush', 'Suitcase', 'SuitcaseLine', 'Umbrella', 'AlarmClock', 'Medal', 'GoldMedal', 'Present', 'Mouse', 'Watch', 'QuartzWatch', 'Magnet', 'Help', 'Soccer', 'ToiletPaper', 'ReadingLamp', 'Paperclip', 'MagicStick', 'Basketball', 'Baseball', 'Coin', 'Goods', 'Sell', 'SoldOut', 'Key', 'ShoppingCart', 'ShoppingCartFull', 'ShoppingTrolley', 'Phone', 'Scissor', 'Handbag', 'ShoppingBag', 'Trophy', 'TrophyBase', 'Stopwatch', 'Timer', 'CollectionTag', 'TakeawayBox', 'PriceTag', 'Wallet', 'Opportunity', 'PhoneFilled', 'WalletFilled', 'GoodsFilled', 'Flag', 'BrushFilled', 'Briefcase', 'Stamp', 'Sunrise', 'Sunny', 'Ship', 'MostlyCloudy', 'PartlyCloudy', 'Sunset', 'Drizzling', 'Pouring', 'Cloudy', 'Moon', 'MoonNight', 'Lightning', 'ChromeFilled', 'Eleme', 'ElemeFilled', 'ElementPlus', 'Shop', 'SwitchFilled', 'WindPower']
const selectItems = iconList.map(icon => {
  return {
    value: icon,
    label: icon,
  }
})

const value = ref('Plus')
const model = defineModel()
// console.log(model.value);

</script>
<template>
  <el-select v-model="value" :suffix-icon="value" filterable placeholder="Select" style="width: 240px">
    <el-option v-for="item in selectItems" :key="item.value" :label="item.label" :value="item.value">
      <el-space size="large">
        <el-icon :size="16">
          <component :is="item.value"></component>
        </el-icon>
        <span>{{ item.label }}</span>
      </el-space>
    </el-option>
  </el-select>
</template>

<style lang="scss" scoped></style>
