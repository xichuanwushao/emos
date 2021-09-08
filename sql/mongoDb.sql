db.message.insert({
 _id: ObjectId("87737874656661504"),
 uuid:"89995738783813632",
 senderId:0,
 senderPhoto:"https://thirdwx.qlogo.cn/mmopen/vi_32/mJQ68h2NfxXIm62ibSB9X8dRM8NCTZvFsW9Cv34B2pwKGJuylOn11picMDCgFsjxGiaWq71xTZia1uCfatDAWr2U5g/132",
 senderName:"Emos系统",
 msg:"HelloWorld",
 sendTime: ISODate("2021-01-23T17:21:30Z")
});
db.message_ref.insert({
 _id: ObjectId("80996285888827392"),
 messageId:"87737874656661504",
 receiverId: 1,
 readFlag: false,
 lastFlag: true
});
db.message.aggregate([
    {
        $set: {
            "id": { $toString: "$_id" }
        }
    },{
        $lookup:{
            from:"message_ref",
            localField:"id",
            foreignField:"messageId",
            as:"ref"
        },
    },
    {$match:{"ref.receiverId": 1 } },
    {$sort:{ sendTime: -1 }},
    {$skip: 0 },
    {$limit: 50 }
])
