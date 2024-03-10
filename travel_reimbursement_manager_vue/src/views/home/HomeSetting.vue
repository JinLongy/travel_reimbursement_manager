<template>
  <div>
    <el-dialog title="添加银行卡" :visible.sync="showDialog">
      <el-form :model="currentCardForm" :rules="rules">
        <div class="inline-group">
          <el-form-item label="户名" class="width-45" prop="userName">
            <el-input v-model="currentCardForm.userName"></el-input>
          </el-form-item>
          <el-form-item label="银行名称" class="width-45" prop="bankName">
            <el-input v-model="currentCardForm.bankName"></el-input>
          </el-form-item>
        </div>
        <div class="inline-group">
          <el-form-item label="卡号" class="width-45" prop="cardNumber">
            <el-input v-model="currentCardForm.cardNumber"></el-input>
          </el-form-item>
          <el-form-item label="开户行地区" class="width-45" prop="region">
            <el-input v-model="currentCardForm.region"></el-input>
          </el-form-item>
        </div>
        <div class="inline-group">
          <el-form-item label="分行名称" class="width-45" prop="branchName">
            <el-input v-model="currentCardForm.branchName"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button @click="saveCard" class="submit-btn">确 定</el-button>
      </span>
    </el-dialog>

    <el-card class="common-card">
      <div class="header">
        <div class="main-title-row">银行卡维护</div>
        <el-button @click="showDialog = true" class="submit-btn">添加银行卡</el-button>
      </div>

      <div class="card-wrapper">
        <div class="card-item" v-for="(card, index) in cards" :key="index">
          <h2 class="bank-name">
            <span>
              {{ card.bankName }}
            </span>
            <el-button type="text" @click="editCard(index)" icon="el-icon-edit" class="edit-btn">编辑</el-button>
          </h2>
          <el-button size="mini" @click="deleteCard(index)" class="del-btn">删除银行卡</el-button>
          <p class="card-no-row">
            <span class="card-no" v-if="card.showCardNumber">{{ card.cardNumber }}</span>
            <span class="card-no" v-else>**** **** **** ****</span>
            <span class="eye-icon">
              <i class="el-icon-view" @click="toggleCardNumber(index)"></i>
            </span>
          </p>
          <p class="user-name">持卡人：{{ card.userName }}</p>
          <p class="card-footer">
            <span>添加时间：{{ card.userName }}</span>
            <span>开户行地区：{{ card.region || '-' }}</span>
          </p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showDialog: false,
      currentCardForm: {
        userName: '',
        bankName: '',
        cardNumber: '',
        region: '',
        branchName: '',
      },
      cards: [
        {
          userName: '111',
          bankName: '222',
          cardNumber: '333',
          region: '444',
          branchName: '555',
          showCardNumber: false,
        },
      ],
      rules: {
        userName: [{ required: true, message: '请输入户名', trigger: 'blur' }],
        bankName: [{ required: true, message: '请输入银行名称', trigger: 'change' }],
        cardNumber: [{ required: true, message: '请输入银行卡号', trigger: 'change' }],
        region: [{ required: true, message: '请选择开户行地区', trigger: 'change' }],
        branchName: [{ required: true, message: '请输入分行名称', trigger: 'change' }],
      },
    }
  },
  methods: {
    saveCard() {
      if (this.currentCardFormIndex !== null) {
        // 编辑现有的卡
        this.cards.splice(this.currentCardFormIndex, 1, { ...this.currentCardForm, showCardNumber: false })
      } else {
        // 添加新的卡
        this.cards.push({ ...this.currentCardForm, showCardNumber: false })
      }
      this.cancel()
    },
    editCard(index) {
      this.currentCardForm = { ...this.cards[index] }
      this.currentCardFormIndex = index
      this.showDialog = true
    },
    deleteCard(index) {
      this.cards.splice(index, 1)
    },
    cancel() {
      this.currentCardForm = {
        userName: '',
        bankName: '',
        cardNumber: '',
        region: '',
        branchName: '',
      }
      this.currentCardFormIndex = null
      this.showDialog = false
    },
    toggleCardNumber(index) {
      this.cards[index].showCardNumber = !this.cards[index].showCardNumber
    },
  },
}
</script>

<style scoped lang="less">
.header {
  .flex_arrange(row, space-between);
  margin-bottom: 20px;
  .main-title-row {
    font-size: 18px;
    text-align: left;
    font-weight: 900;
  }
}
.card-wrapper {
  display: flex;
  flex-wrap: wrap;
  .card-item {
    width: 350px;
    border-radius: 8px;
    padding: 16px;
    box-sizing: border-box;
    background: linear-gradient(to left top, #ff6873, #ff716a); /* 淡红色背景 */
    margin-right: 30px;
    margin-bottom: 30px;
    position: relative;
    .bank-name {
      color: @white-color;
      font-size: 16px;
      font-weight: 600;
      .flex_arrange(row, space-between);
      .edit-btn {
        color: @white-color;
      }
    }
    .card-no-row {
      font-size: 32px;
      margin: 30px 0;
      color: @white-color;
      .flex_arrange(row, flex-start);
      .card-no {
        margin-bottom: -10px;
      }
      .eye-icon {
        margin-left: 16px;
        font-size: 20px;
      }
    }
    .user-name {
      color: @white-color;
      font-size: 18px;
      font-weight: 600;
    }
    .card-footer {
      margin-top: 8px;
      font-size: 12px;
      color: @white-color;
      span + span {
        margin-left: 58px;
      }
    }
    .del-btn {
      position: absolute;
      top: 50px;
      right: 18px;
    }
  }
}

.inline-group {
  .flex_arrange(row, space-between);
  /* 在表单项之间添加间距 */
  .width-45 {
    width: 45%;
  }
  .width-100 {
    width: 100%;
  }
}
/deep/ .el-form-item__label {
  font-weight: 600;
  color: #333;
}
.submit-btn {
  background: @primary-color;
  color: @white-color;
}
</style>
