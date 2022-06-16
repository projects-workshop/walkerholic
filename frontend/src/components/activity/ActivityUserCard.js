import moment from 'moment'
import React from 'react'
import { useSelector } from 'react-redux'
import { Link,useHistory } from 'react-router-dom'
import basicProfile from '../../images/basicProfile.svg'

function ActivityUserCard({activity}) {
    const auth = useSelector(state => state.auth)
    const history = useHistory()

    const handleOnClick = (userId)=>{
        if(auth.user){
            history.push(`/user/${userId}`)
        }else{
            history.push('/signin')
        }
    }
    return (
        <div className="activityuser_card" onClick={()=>handleOnClick(activity.userId)}>
            {/* <Link to={`/user/${activity.userId}`}> */}
                <div style={activity.status==='ONGOING'? {color:"#e6857e"}:{color:"#58c4a9"}} className="activityuser_card_status">
                    {activity.status}
                </div>
                <div className="activityuser_card_image">
                    <img src={activity.userImageUrl? activity.userImageUrl : basicProfile} alt="" />
                </div>
                <div className="activityuser_card_name">
                    {activity.userFullname}
                </div>

                {
                    activity.status==='FINISHED' &&
                    <div className="activityuser_card_time">
                        {moment(activity.updatedAt).format('L')}
                    </div>
                }
            {/* </Link> */}
        </div>
    )
}

export default ActivityUserCard
